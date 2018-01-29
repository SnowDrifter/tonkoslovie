package ru.romanov.tonkoslovie.oauth.extractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.SocialMedia;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.*;

@Component
public class VkPrincipalExtractor implements PrincipalExtractor {

    private final UserRepository userRepository;

    @Autowired
    public VkPrincipalExtractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object extractPrincipal(Map<String, Object> map) {
        User user;

        List<LinkedHashMap> users = (List<LinkedHashMap>) map.get("response");
        LinkedHashMap<String, Object> vkUserDetails = users.get(0);

        if (userRepository.existsByEmail((String) map.get("email"))) {
            user = userRepository.findFirstByEmail((String) map.get("email"));
        } else {
            user = new User();
            user.setEmail((String) map.get("email"));
            user.setFirstName((String) vkUserDetails.get("first_name"));
            user.setLastName((String)  vkUserDetails.get("last_name"));
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            user.setPassword(UUID.randomUUID().toString());
            user.setCreationDate(new Date());
            user.setEnabled(true);

            SocialMedia socialMedia = new SocialMedia();
            socialMedia.setVkId((Integer) vkUserDetails.get("uid"));
            socialMedia.setVkBirthDate((String) vkUserDetails.get("bdate"));
            socialMedia.setVkSex((Integer) vkUserDetails.get("sex"));
            socialMedia.setVkPhoto((String) vkUserDetails.get("photo_max_orig"));
            socialMedia.setVkFriendStatus((Integer) vkUserDetails.get("friend_status"));
            socialMedia.setVkRelation((Integer) vkUserDetails.get("relation"));
            socialMedia.setVkRelationPartner((Integer) vkUserDetails.get("relation_partner"));

            user.setSocialMedia(socialMedia);

            userRepository.save(user);
        }

        return user;
    }
}
