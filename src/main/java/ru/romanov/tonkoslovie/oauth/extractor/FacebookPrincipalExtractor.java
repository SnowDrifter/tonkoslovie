package ru.romanov.tonkoslovie.oauth.extractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.user.UserRepository;

import java.util.Map;

@Component
public class FacebookPrincipalExtractor implements PrincipalExtractor {

    private final UserRepository userRepository;

    @Autowired
    public FacebookPrincipalExtractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        return null;
    }
}
