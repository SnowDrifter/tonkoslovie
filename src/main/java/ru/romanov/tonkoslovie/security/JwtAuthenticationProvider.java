package ru.romanov.tonkoslovie.security;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.security.user.AuthUser;
import ru.romanov.tonkoslovie.user.UserRepository;


@Service
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString().replace("Bearer ", "");
        logger.debug("Auth token: {}", token);

        if (Jwts.parser().isSigned(token)) {
            AuthUser authCustomUser = authService.convert(token);;

            if (authCustomUser != null && userRepository.existsByToken(token)) {
                PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(authCustomUser, authentication.getCredentials(), authCustomUser.getAuthorities());
                result.setDetails(authentication.getDetails());
                return result;
            }
        } else {
            logger.debug("Invalid token format");
        }
        logger.debug("Cannot authorize user");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }
}