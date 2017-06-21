package ru.romanov.tonkoslovie.security;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.security.user.AuthUser;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${auth.jwt.secret:test}")
    private String secret;
    private Key key;

    @PostConstruct
    public void init() {
        key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public AuthUser convert(String token) {
        try {
            Jwt authToken = Jwts.parser().setSigningKey(key).parse(token);
            Map<String, String> authTokenParams = (Map<String, String>) authToken.getBody();
            if (authTokenParams.containsKey("roles")) {
                return new AuthUser(Long.parseLong(authTokenParams.get("userId")), authTokenParams.get("roles"));
            }

            return new AuthUser(Long.parseLong(authTokenParams.get("userId")), "ROLE_USER");
        } catch (Exception e) {
            logger.error("Convert jwt exception: {}", e.toString());
        }
        return null;
    }

    @Override
    public String makeToken(String userId, String roles, Map<String, Object> params) {
        Map<String, Object> claims = new HashMap<>();
        if (StringUtils.hasText(roles)) {
            claims.put("roles", roles);
        }

        claims.put("userId", userId);
        if (params != null) {
            claims.putAll(params);
        }

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
