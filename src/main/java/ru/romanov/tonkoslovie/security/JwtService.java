package ru.romanov.tonkoslovie.security;


import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Map;

public interface JwtService {

    User convert(String token);

    String makeToken(String userId, String roles, Map<String, Object> params);

    default String makeToken(String userId, String roles) {
        return makeToken(userId, roles, null);
    }

    default String makeToken(String userId) {
        return makeToken(userId, null, null);
    }

}
