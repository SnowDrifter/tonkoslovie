package ru.romanov.tonkoslovie.security;


import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Map;
import java.util.Set;

public interface JwtService {

    User convert(String token);

    String makeToken(long userId, Set<Role> roles, Map<String, Object> params);

    default String makeToken(long userId, Set<Role> roles) {
        return makeToken(userId, roles, null);
    }

    boolean isValid(String token);

}
