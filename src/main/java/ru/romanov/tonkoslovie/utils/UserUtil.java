package ru.romanov.tonkoslovie.utils;


import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import ru.romanov.tonkoslovie.user.entity.Role;

import java.util.Set;

public class UserUtil {

    public static final String ROLES_DELIMITER = ",";
    public static final String OAUTH_USER_ID_ATTRIBUTE = "tonkoslovie_user_id";

    public static String convertRoles(Set<Role> roles) {
        StringBuilder rolesBuilder = new StringBuilder();
        roles.forEach(role -> rolesBuilder.append(role.getAuthority()).append(ROLES_DELIMITER));
        return rolesBuilder.substring(0, rolesBuilder.length() - 1);
    }

    public static String getUserNameAttributeName(OAuth2UserRequest request) {
        return request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    }

}
