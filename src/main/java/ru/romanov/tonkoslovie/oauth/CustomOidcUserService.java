package ru.romanov.tonkoslovie.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.oauth.converter.service.UserConverterService;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.HashMap;
import java.util.Map;

import static ru.romanov.tonkoslovie.utils.UserHelper.OAUTH_USER_ID_ATTRIBUTE;

@Component
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final UserConverterService userConverterService;

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        User user = findOrCreateUser(oidcUser, registrationId);

        Map<String, Object> attributes = new HashMap<>(oidcUser.getUserInfo().getClaims());
        attributes.put(OAUTH_USER_ID_ATTRIBUTE, user.getId());

        return new DefaultOidcUser(user.getRoles(), oidcUser.getIdToken(), new OidcUserInfo(attributes));
    }

    private User findOrCreateUser(OidcUser oidcUser, String registrationId) {
        String email = oidcUser.getEmail();
        if (userRepository.existsByEmail(email)) {
            return userRepository.findFirstByEmail(email);
        } else {
            User user = userConverterService.convert(oidcUser, registrationId);
            return userRepository.save(user);
        }
    }
}
