package ru.romanov.tonkoslovie.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void createRoot() {
        User root = findByUsername("root");

        if (root == null) {
            root = new User();
            root.setUsername("root");
            root.setPassword(passwordEncoder.encode("1q2w3e4r"));
            root.setRoles(new HashSet<>(Arrays.asList(Role.values())));
            userRepository.save(root);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        if (StringUtils.hasText(username)) {
            return userRepository.findByUsername(username);
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

        userRepository.save(user);
    }

    @Override
    public long countByUsername(String username) {
        return userRepository.countByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }
}
