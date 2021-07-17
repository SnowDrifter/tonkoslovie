package ru.romanov.tonkoslovie.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.mail.EmailService;
import ru.romanov.tonkoslovie.mail.EmailVerificationRepository;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.security.JwtService;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.exception.ValidationException;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;
import ru.romanov.tonkoslovie.user.web.response.ValidationError;

import javax.servlet.http.HttpServletResponse;
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
    @Transactional(readOnly = true)
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
    public User update(long userId, UserRequest request) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Пользователь не найден");
        }

        User user = userRepository.getOne(userId);

        if (StringUtils.hasText(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }

        if (StringUtils.hasText(request.getLastName())) {
            user.setLastName(request.getLastName());
        }

        if (StringUtils.hasText(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        return userRepository.save(user);
    }

    @Override
    public void confirmRegistration(UUID token, HttpServletResponse response) throws IOException {
        EmailVerification verification = emailVerificationRepository.findByToken(token);

        if (verification != null && verification.getExpirationDate().after(new Date())) {
            User user = verification.getUser();
            user.setEnabled(true);
            userRepository.save(user);

            String jwtToken = jwtService.makeToken(user.getId(), user.getRoles());
            response.sendRedirect(siteHost + "/registration/success?token=" + jwtToken);
        } else {
            response.sendRedirect(siteHost + "/registration/error");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(email);
    }

}
