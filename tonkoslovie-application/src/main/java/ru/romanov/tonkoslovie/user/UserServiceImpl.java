package ru.romanov.tonkoslovie.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanov.tonkoslovie.hibernate.specification.GenericSpecificationBuilder;
import ru.romanov.tonkoslovie.mail.EmailService;
import ru.romanov.tonkoslovie.mail.EmailVerificationRepository;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.security.JwtService;
import ru.romanov.tonkoslovie.user.dto.UserDto;
import ru.romanov.tonkoslovie.user.dto.UserMapper;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.exception.ValidationException;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;
import ru.romanov.tonkoslovie.user.web.response.ValidationError;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${app.site-host}")
    private String siteHost;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JwtService jwtService;

    @Override
    public Page<UserDto> searchUsers(int page, int size, String searchQuery) {
        Specification<User> specification = new GenericSpecificationBuilder<User>()
                .addParametersFromSearchQuery(searchQuery)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.findAll(specification, pageable);
        return users.map(UserMapper.INSTANCE::toDto);
    }

    @Override
    public UserResponse login(UserRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail());
        if (user == null) {
            return new UserResponse(null, "Пользователь не найден");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new UserResponse(null, "Неправильный пароль");
        }

        String token = jwtService.makeToken(user.getId(), user.getRoles());
        return new UserResponse(token, null);
    }

    @Override
    @Transactional
    public void saveNewUser(User user) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (userRepository.existsByEmail(user.getEmail())) {
            validationErrors.add(new ValidationError("email", "Пользователь с таким адресом электронной почты уже зарегестрирован"));
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(new Date());

        userRepository.save(user);

        new Thread(() -> emailService.sendVerification(user)).start();
    }

    @Override
    @Transactional
    public UserDto update(long userId, UserDto userDto) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Пользователь не найден");
        }

        User user = userRepository.getById(userId);
        UserMapper.INSTANCE.updateUserFromDto(user, userDto);
        user = userRepository.save(user);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    @Transactional
    public void confirmRegistration(UUID token, HttpServletResponse response) {
        EmailVerification verification = emailVerificationRepository.findByToken(token);

        try {
            if (verification != null && verification.getExpirationDate().after(new Date())) {
                User user = verification.getUser();
                user.setEnabled(true);
                userRepository.save(user);

                String jwtToken = jwtService.makeToken(user.getId(), user.getRoles());

                response.sendRedirect(siteHost + "/registration/success?token=" + jwtToken);
            } else {
                response.sendRedirect(siteHost + "/registration/error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(email);
    }

}
