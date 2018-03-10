package ru.romanov.tonkoslovie.utils;


import ru.romanov.tonkoslovie.user.entity.Role;

import java.util.Set;

public class UserHelper {

    public final static String ROLES_DELIMITER = ",";

    public static String convertRoles(Set<Role> roles) {
        StringBuilder rolesBuilder = new StringBuilder();
        roles.forEach(role -> rolesBuilder.append(role.getAuthority()).append(ROLES_DELIMITER));
        return rolesBuilder.substring(0, rolesBuilder.length() - 1);
    }

}
