package ru.romanov.tonkoslovie.oauth.converter;

import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Map;

public interface OAuthUserConverter {

    String getServiceName();

    User convert(Map<String, Object> attributes);

}
