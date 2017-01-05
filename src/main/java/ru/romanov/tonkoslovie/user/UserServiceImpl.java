package ru.romanov.tonkoslovie.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.mail.EmailVerificationRepository;
import ru.romanov.tonkoslovie.mail.MailService;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailService mailService;
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

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
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (StringUtils.hasText(username)) {
            return userRepository.findFirstByUsername(username);
        } else {
            return null;
        }
    }

    @Override
    public void saveNewUser(User user) {
        Set<Role> baseRoles = new HashSet<>();

        baseRoles.add(Role.ROLE_USER);
        user.setRoles(baseRoles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String token = UUID.randomUUID().toString();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiryDate(new Date());

        userRepository.save(user);
        emailVerificationRepository.save(verification);
        mailService.sendVerification(user.getEmail(), token);
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

        if(verification != null){
            User user = verification.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        } else{
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findFirstByUsername(username);
    }

    private User updateFields(User user){
        User oldUser = userRepository.getOne(user.getId());

        if(user.getFirstName() != null){
            oldUser.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null){
            oldUser.setLastName(user.getLastName());
        }

        return oldUser;
    }
}
