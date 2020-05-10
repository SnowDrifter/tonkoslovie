package ru.romanov.tonkoslovie.oauth.converter.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.romanov.tonkoslovie.user.entity.User;

public interface UserConverterService {

    User convert(OAuth2User oauth2User, String registrationId);

}
