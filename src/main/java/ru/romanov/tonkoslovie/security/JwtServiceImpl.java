package ru.romanov.tonkoslovie.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.user.entity.Role;
import ru.romanov.tonkoslovie.user.entity.User;
import ru.romanov.tonkoslovie.utils.UserHelper;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;
    private Key key;
    private JwtParser jwtParser;
    private static final String JWT_USER_ID_KEY = "userId";
    private static final String JWT_ROLES_KEY = "roles";
    private static final String JWT_SALT_KEY = "s";

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    @Override
    public User convert(String token) {
        try {
            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            Long userId = claims.get(JWT_USER_ID_KEY, Long.class);
            String roles = claims.get(JWT_ROLES_KEY, String.class);

            return new User(userId, roles);
        } catch (Exception e) {
            log.error("Parse jwt error. {}: {}", e.getClass().getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Override
    public String makeToken(long userId, Set<Role> roles, Map<String, Object> params) {
        Claims claims = Jwts.claims();
        claims.put(JWT_USER_ID_KEY, userId);
        claims.put(JWT_ROLES_KEY, UserHelper.convertRoles(roles));
        claims.put(JWT_SALT_KEY, System.currentTimeMillis());

        if (params != null) {
            claims.putAll(params);
        }

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isValid(String token) {
        return jwtParser.isSigned(token);
    }
}
