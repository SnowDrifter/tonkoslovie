package ru.romanov.tonkoslovie.oauth.converter;

import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.user.entity.SocialMedia;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
public class FacebookOAuthUserConverter implements OAuthUserConverter {

    @Override
    public String getServiceName() {
        return "facebook";
    }

    @Override
    public User convert(Map<String, Object> attributes) {
        User user = new User();
        user.setEmail((String) attributes.get("email"));
        user.setFirstName((String) attributes.get("first_name"));
        user.setLastName((String) attributes.get("last_name"));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setPassword(UUID.randomUUID().toString());
        user.setCreationDate(new Date());
        user.setEnabled(true);

        SocialMedia socialMedia = new SocialMedia();
        socialMedia.setFacebookId((String) attributes.get("id"));
        user.setSocialMedia(socialMedia);

        return user;
    }
}
