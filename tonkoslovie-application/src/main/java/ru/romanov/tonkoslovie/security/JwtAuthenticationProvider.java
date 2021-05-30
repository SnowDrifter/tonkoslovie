package ru.romanov.tonkoslovie.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.user.entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString().replace("Bearer ", "");
        log.debug("Auth token: {}", token);

        if (jwtService.isValid(token)) {
            User user = jwtService.convert(token);

            if (user != null) {
                PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
                result.setDetails(authentication.getDetails());
                return result;
            }
        } else {
            log.debug("Invalid token format");
        }
        log.debug("Cannot authorize user");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}