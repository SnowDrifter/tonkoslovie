package ru.romanov.tonkoslovie.security;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.security.user.AuthUser;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.security.redis-prefix}")
    private String redisSecurityPrefix;
    @Value("${auth.jwt.secret:test}")
    private String secret;
    private Key key;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AuthServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

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
            e.printStackTrace();
            log.error("Convert jwt exception: {}", e.toString());
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

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        saveToRedis(token, new AuthUser(Long.valueOf(userId), roles));

        return token;
    }

    @Override
    public void saveToRedis(String token, AuthUser user) {
        try {
            redisTemplate.boundValueOps(redisSecurityPrefix + token).set(user);
        } catch (Exception e) {
            log.error("Redis save error: {}", e.toString());
        }
    }

    @Override
    public boolean logoutFromRedis(String token) {
        try {
            redisTemplate.delete(redisSecurityPrefix + token);
            return true;
        } catch (Exception e) {
            log.error("Redis logout error: {}", e.toString());
        }
        return false;
    }

}
