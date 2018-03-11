package ru.romanov.tonkoslovie.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.romanov.tonkoslovie.user.web.request.UserRequest;
import ru.romanov.tonkoslovie.user.web.response.RegistrationResponse;
import ru.romanov.tonkoslovie.user.web.response.UserResponse;
import ru.romanov.tonkoslovie.user.web.response.ValidationError;
import ru.romanov.tonkoslovie.utils.UserHelper;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Value("${app.siteHost}")
    private String siteHost;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JwtService jwtService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, EmailVerificationRepository emailVerificationRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.jwtService = jwtService;
    }

    @PostConstruct
    @Transactional
    public void createRoot() {
        User root = userRepository.findFirstByEmail("root");

        if (root == null) {
            root = new User();
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("1q2w3e4r"));
            root.setRoles(new HashSet<>(Arrays.asList(Role.values())));
            root.setEnabled(true);
            root.setEmail("root");
            userRepository.save(root);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<UserResponse> login(UserRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail());
        if (user == null) {
            return new ResponseEntity<>(new UserResponse(null, "Пользователь не найден"), HttpStatus.BAD_REQUEST);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(new UserResponse(null, "Неправильный пароль"), HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.makeToken(String.valueOf(user.getId()), UserHelper.convertRoles(user.getRoles()), Collections.singletonMap("s", System.currentTimeMillis()));
        user.setToken(token);
        userRepository.save(user);

        return new ResponseEntity<>(new UserResponse(token, null), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RegistrationResponse> saveNewUser(User user) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (userRepository.existsByEmail(user.getEmail())) {
            validationErrors.add(new ValidationError("email", "Пользователь с таким адресом электронной почты уже зарегестрирован"));
        }

        if (!validationErrors.isEmpty()) {
            return new ResponseEntity<>(new RegistrationResponse(validationErrors), HttpStatus.BAD_REQUEST);
        }

        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreationDate(new Date());

        userRepository.save(user);

        new Thread(() -> emailService.sendVerification(user)).start();

        return ResponseEntity.ok().build();
    }

    @Override
    public User update(User user) {
        user = updateFields(user);
        return userRepository.save(user);
    }

    @Override
    public void confirmRegistration(String token, HttpServletResponse response) throws IOException {
        EmailVerification verification = emailVerificationRepository.findByToken(token);

        if (verification != null) {
            User user = verification.getUser();
            user.setEnabled(true);

            String authToken = jwtService.makeToken(String.valueOf(user.getId()), UserHelper.convertRoles(user.getRoles()), Collections.singletonMap("s", System.currentTimeMillis()));
            userRepository.save(user);

            response.sendRedirect(siteHost + "/registration/success?token=" + authToken);
        } else {
            response.sendRedirect(siteHost + "/registration/error");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findFirstByEmail(email);
    }

    private User updateFields(User user) {
        User oldUser = userRepository.getOne(user.getId());

        if (StringUtils.hasText(user.getFirstName())) {
            oldUser.setFirstName(user.getFirstName());
        }

        if (StringUtils.hasText(user.getLastName())) {
            oldUser.setLastName(user.getLastName());
        }

        if (StringUtils.hasText(user.getUsername())) {
            oldUser.setUsername(user.getUsername());
        }

        oldUser.setRoles(user.getRoles());
        oldUser.setEnabled(user.isEnabled());

        return oldUser;
    }
}
