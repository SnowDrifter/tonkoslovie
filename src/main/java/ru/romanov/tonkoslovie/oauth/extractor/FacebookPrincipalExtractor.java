package ru.romanov.tonkoslovie.oauth.extractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
public class FacebookPrincipalExtractor implements PrincipalExtractor {

    private final UserRepository userRepository;

    @Autowired
    public FacebookPrincipalExtractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        User user;

        if (userRepository.existsByEmail((String) map.get("email"))) {
            user = userRepository.findFirstByEmail((String) map.get("email"));
        } else {
            user = new User();
            user.setEmail((String) map.get("email"));
            user.setFirstName((String) map.get("first_name"));
            user.setFirstName((String) map.get("last_name"));
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            user.setPassword(UUID.randomUUID().toString());
            user.setEnabled(true);
            userRepository.save(user);
        }

        return user;
    }
}
