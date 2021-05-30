package ru.romanov.tonkoslovie.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
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
import org.springframework.web.util.UriComponentsBuilder;
import ru.romanov.tonkoslovie.user.UserRepository;
import ru.romanov.tonkoslovie.oauth.converter.service.UserConverterService;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.utils.UserUtil;

import java.util.*;

import static ru.romanov.tonkoslovie.utils.UserUtil.OAUTH_USER_ID_ATTRIBUTE;

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

        String userNameAttributeName = UserUtil.getUserNameAttributeName(request);
        return new DefaultOAuth2User(user.getAuthorities(), attributes, userNameAttributeName);
    }

    @SuppressWarnings("unchecked")
    private OAuth2User loadVkUser(OAuth2UserRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            String url = getVkUserRequestUrl(request);
            ResponseEntity<Object> entity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);

            Map<String, Object> response = (Map<String, Object>) entity.getBody();
            if (response == null) {
                throw new RuntimeException("Cannot get response");
            }

            List<Map<String, Object>> valueList = (List<Map<String, Object>>) response.get("response");
            Map<String, Object> attributes = valueList.get(0);
            attributes.putAll(request.getAdditionalParameters());

            String userNameAttributeName = UserUtil.getUserNameAttributeName(request);
            Set<GrantedAuthority> authorities = Set.of(new OAuth2UserAuthority(attributes));
            return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
        } catch (HttpClientErrorException e) {
            log.error("Cannot load vk user. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getVkUserRequestUrl(OAuth2UserRequest request) {
        String url = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

        return UriComponentsBuilder.fromUriString(url)
                .queryParam("access_token", request.getAccessToken().getTokenValue())
                .build()
                .toUriString();
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
