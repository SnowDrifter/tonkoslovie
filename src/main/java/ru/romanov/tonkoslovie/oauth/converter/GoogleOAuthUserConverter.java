package ru.romanov.tonkoslovie.oauth.converter;

import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.SocialMedia;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class GoogleOAuthUserConverter implements OAuthUserConverter {

    @Override
    public User convert(Map<String, Object> attributes) {
        User user = new User();
        user.setEmail((String) attributes.get("email"));
        user.setFirstName((String) attributes.get("given_name"));
        user.setLastName((String) attributes.get("family_name"));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(UUID.randomUUID().toString());
        user.setCreationDate(new Date());
        user.setEnabled(true);

        SocialMedia socialMedia = new SocialMedia();
        socialMedia.setGoogleId((String) attributes.get("sub"));
        socialMedia.setGooglePhoto((String) attributes.get("picture"));
        user.setSocialMedia(socialMedia);

        return user;
    }
}
