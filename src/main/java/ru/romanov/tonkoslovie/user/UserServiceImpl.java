package ru.romanov.tonkoslovie.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.mail.EmailVerificationRepository;
import ru.romanov.tonkoslovie.mail.MailService;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.security.AuthService;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.RegistrationResponse;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthService authService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService, EmailVerificationRepository emailVerificationRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.authService = authService;
    }

    @PostConstruct
    @Transactional
    public void createRoot() {
        User root = findByUsername("root");

        if (root == null) {
            root = new User();
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("1q2w3e4r"));
            root.setRoles(new HashSet<>(Arrays.asList(Role.values())));
            root.setEnabled(true);
            root.setEmail("mail@mail.mail");
            userRepository.save(root);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> login(UserRequest request) {
        User user = userRepository.findFirstByUsername(request.getUsername());
        if (user == null) {
            return new ResponseEntity<>(new UserResponse(null, "Пользователь не найден"), HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(new UserResponse(null, "Неправильный пароль"), HttpStatus.BAD_REQUEST);
        }

        StringBuilder roles = new StringBuilder();
        user.getAuthorities().forEach(role -> roles.append(role.getAuthority()).append(", "));

        String token = authService.makeToken(String.valueOf(user.getId()), roles.substring(0, roles.length() - 2), Collections.singletonMap("s", System.currentTimeMillis()));
        user.setToken(token);
        userRepository.save(user);

       return new ResponseEntity<>(new UserResponse(token, null), HttpStatus.OK);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (StringUtils.hasText(username)) {
            return userRepository.findFirstByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public RegistrationResponse saveNewUser(User user) {
        Set<Role> baseRoles = new HashSet<>();

        baseRoles.add(Role.ROLE_USER);
        user.setRoles(baseRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        try {
            mailService.sendVerification(user);
        } catch (Exception e) {
            log.error("Sending email error, message: {}, email: {}", e.getMessage(), user.getEmail());
            return new RegistrationResponse("Возникла ошибка при отправке письма на электронную почту!");
        }

        return new RegistrationResponse();
    }

    @Override
    public User update(User user) {
        user = updateFields(user);
        return userRepository.save(user);
    }

    @Override
    public long countByUsername(String username) {
        return userRepository.countByUsername(username);
    }

    @Override
    public boolean checkToken(String token) {
        EmailVerification verification = emailVerificationRepository.findByToken(token);

        if (verification != null) {
            User user = verification.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username);
    }

    private User updateFields(User user) {
        User oldUser = userRepository.getOne(user.getId());

        if (user.getFirstName() != null) {
            oldUser.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            oldUser.setLastName(user.getLastName());
        }

        return oldUser;
    }
}
