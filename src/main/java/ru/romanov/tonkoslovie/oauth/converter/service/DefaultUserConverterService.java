package ru.romanov.tonkoslovie.oauth.converter.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.oauth.converter.FacebookOAuthUserConverter;
import ru.romanov.tonkoslovie.oauth.converter.GoogleOAuthUserConverter;
import ru.romanov.tonkoslovie.oauth.converter.OAuthUserConverter;
import ru.romanov.tonkoslovie.oauth.converter.VkOAuthUserConverter;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultUserConverterService implements UserConverterService {

    private final Map<String, OAuthUserConverter> extractors = new HashMap<>();

    @PostConstruct
    public void init() {
        extractors.put("vk", new VkOAuthUserConverter());
        extractors.put("facebook", new FacebookOAuthUserConverter());
        extractors.put("google", new GoogleOAuthUserConverter());
    }

    @Override
    public User convert(OAuth2User oauth2User, String registrationId) {
        OAuthUserConverter extractor = extractors.get(registrationId);
        if (extractor == null) {
            throw new IllegalArgumentException("Unsupported registration id: " + registrationId);
        }

        return extractor.convert(oauth2User.getAttributes());
    }
}
