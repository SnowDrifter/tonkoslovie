package ru.romanov.tonkoslovie.oauth.converter.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.oauth.converter.OAuthUserConverter;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserConverterServiceImpl implements UserConverterService {

    private final Map<String, OAuthUserConverter> converters;

    public UserConverterServiceImpl(List<OAuthUserConverter> converters) {
        this.converters = converters.stream()
                .collect(Collectors.toMap(OAuthUserConverter::getServiceName, c -> c));
    }

    @Override
    public User convert(OAuth2User oauth2User, String service) {
        OAuthUserConverter converter = converters.get(service);
        if (converter == null) {
            throw new IllegalArgumentException("Unsupported service: " + service);
        }

        return converter.convert(oauth2User.getAttributes());
    }
}
