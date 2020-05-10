package ru.romanov.tonkoslovie.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.romanov.tonkoslovie.oauth.converter.service.UserConverterService;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.utils.UserHelper;

import java.util.*;

import static ru.romanov.tonkoslovie.utils.UserHelper.OAUTH_USER_ID_ATTRIBUTE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserConverterService userConverterService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        String registrationId = request.getClientRegistration().getRegistrationId();

        OAuth2User oauth2User;
        if (registrationId.equals("vk")) {
            oauth2User = loadVkUser(request);
        } else {
            oauth2User = super.loadUser(request);
        }

        User user = findOrCreateUser(oauth2User, registrationId);

        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.put(OAUTH_USER_ID_ATTRIBUTE, user.getId());

        String userNameAttributeName = UserHelper.getUserNameAttributeName(request);
        return new DefaultOAuth2User(user.getAuthorities(), attributes, userNameAttributeName);
    }

    @SuppressWarnings("unchecked")
    private OAuth2User loadVkUser(OAuth2UserRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<?> httpRequest = new HttpEntity(headers);

            String uri = getVkUserRequestUri(request);
            ResponseEntity<Object> entity = restTemplate.exchange(uri, HttpMethod.GET, httpRequest, Object.class);

            Map<String, Object> response = (Map<String, Object>) entity.getBody();
            ArrayList valueList = (ArrayList) response.get("response");
            Map<String, Object> attributes = (Map<String, Object>) valueList.get(0);
            attributes.putAll(request.getAdditionalParameters());

            String userNameAttributeName = UserHelper.getUserNameAttributeName(request);
            Set<GrantedAuthority> authorities = Set.of(new OAuth2UserAuthority(attributes));
            return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
        } catch (HttpClientErrorException e) {
            log.error("Cannot load vk user. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getVkUserRequestUri(OAuth2UserRequest request) {
        return  request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri()
                + "&access_token=" + request.getAccessToken().getTokenValue();
    }

    private User findOrCreateUser(OAuth2User oauth2User, String registrationId) {
        String email = oauth2User.getAttribute("email");
        if (userRepository.existsByEmail(email)) {
            return userRepository.findFirstByEmail(email);
        } else {
            User user = userConverterService.convert(oauth2User, registrationId);
            return userRepository.save(user);
        }
    }
}
