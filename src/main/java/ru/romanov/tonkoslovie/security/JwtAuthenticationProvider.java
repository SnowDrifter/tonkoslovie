package ru.romanov.tonkoslovie.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.security.user.AuthUser;

@Slf4j
@Service
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString().replace("Bearer ", "");
        log.debug("Auth token: {}", token);

        AuthUser authUser;
        if (Jwts.parser().isSigned(token)) {
            authUser = jwtService.convert(token);

            if(authUser != null) {
                PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(authUser, authentication.getCredentials(), authUser.getAuthorities());
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