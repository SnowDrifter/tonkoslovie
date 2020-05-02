package ru.romanov.tonkoslovie.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.security.JwtService;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${security.oauth2.successRedirectUrl}")
    private String successOauthRedirectUrl;

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();

        String token = jwtService.makeToken(user.getId(), user.getRoles());

        this.setDefaultTargetUrl(successOauthRedirectUrl + "?token=" + token);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
