package ru.romanov.tonkoslovie.security;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.security.user.AuthUser;
import ru.romanov.tonkoslovie.user.UserRepository;


@Slf4j
@Service
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Value("${app.security.redis-prefix}")
    private String redisSecurityPrefix;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public JwtAuthenticationProvider(AuthService authService, UserRepository userRepository, RedisTemplate<String, Object> redisTemplate) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString().replace("Bearer ", "");
        log.debug("Auth token: {}", token);

        AuthUser authUser;
        if (Jwts.parser().isSigned(token)) {
            authUser = (AuthUser) redisTemplate.boundValueOps(redisSecurityPrefix + token).get();

            if (authUser != null) {
                PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(authUser, authentication.getCredentials(), authUser.getAuthorities());
                result.setDetails(authentication.getDetails());
                return result;
            }

            if (userRepository.existsByToken(token)) {
                authUser = authService.convert(token);
                PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(authUser, authentication.getCredentials(), authUser.getAuthorities());
                result.setDetails(authentication.getDetails());

                // Update redis
                authService.saveToRedis(token, authUser);
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