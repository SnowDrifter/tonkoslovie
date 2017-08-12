package ru.romanov.tonkoslovie.security;


import ru.romanov.tonkoslovie.security.user.AuthUser;

import java.util.Map;

public interface AuthService {

    AuthUser convert(String token);

    String makeToken(long userId, String roles, Map<String, Object> params);

    default String makeToken(long userId, String roles){
        return makeToken(userId, roles, null);
    }

    default String makeToken(long userId){
        return makeToken(userId, null, null);
    }

    boolean logoutFromRedis(String token);

}
