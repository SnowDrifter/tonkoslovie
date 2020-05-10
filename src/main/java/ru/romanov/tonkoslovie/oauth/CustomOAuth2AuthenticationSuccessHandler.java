package ru.romanov.tonkoslovie.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.security.JwtService;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.utils.UserHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.siteHost}")
    private String siteHost;

    private final JwtService jwtService;

    @Override
    @SuppressWarnings("unchecked")
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        long userId = oauth2User.getAttribute(UserHelper.OAUTH_USER_ID_ATTRIBUTE);
        Set<Role> roles = (Set<Role>) oauth2User.getAuthorities();

        String token = jwtService.makeToken(userId, roles);

        this.setDefaultTargetUrl(siteHost + "?token=" + token);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
