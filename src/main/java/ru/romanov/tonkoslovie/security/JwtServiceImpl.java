package ru.romanov.tonkoslovie.security;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${auth.jwt.secret}")
    private String secret;
    private Key key;
    private static final String JWT_USER_ID_KEY = "userId";
    private static final String JWT_ROLES_KEY = "roles";
    private static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @PostConstruct
    public void init() {
        key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public User convert(String token) {
        try {
            Jwt authToken = Jwts.parser().setSigningKey(key).parse(token);
            Map<String, String> authTokenParams = (Map<String, String>) authToken.getBody();
            if (authTokenParams.containsKey(JWT_ROLES_KEY)) {
                return new User(Long.parseLong(authTokenParams.get(JWT_USER_ID_KEY)), authTokenParams.get(JWT_ROLES_KEY));
            }

            return new User(Long.parseLong(authTokenParams.get(JWT_USER_ID_KEY)), DEFAULT_USER_ROLE);
        } catch (Exception e) {
            log.error("Convert jwt exception: {}", e.toString());
        }
        return null;
    }

    @Override
    public String makeToken(String userId, String roles, Map<String, Object> params) {
        Map<String, Object> claims = new HashMap<>();
        if (StringUtils.hasText(roles)) {
            claims.put(JWT_ROLES_KEY, roles);
        }

        claims.put(JWT_USER_ID_KEY, userId);
        if (params != null) {
            claims.putAll(params);
        }

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
