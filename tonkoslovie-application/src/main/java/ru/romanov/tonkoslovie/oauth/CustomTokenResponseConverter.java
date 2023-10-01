package ru.romanov.tonkoslovie.oauth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenResponseConverter implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> parameters) {
        String accessToken = (String) parameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
        Integer expiresIn = (Integer) parameters.get(OAuth2ParameterNames.EXPIRES_IN);

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(expiresIn)
                .additionalParameters(new HashMap<>(parameters))
                .build();
    }
}
