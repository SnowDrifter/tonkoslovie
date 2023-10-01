package ru.romanov.tonkoslovie.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import ru.romanov.tonkoslovie.oauth.CustomOAuth2AuthenticationSuccessHandler;
import ru.romanov.tonkoslovie.oauth.CustomOAuth2UserService;
import ru.romanov.tonkoslovie.oauth.CustomOidcUserService;
import ru.romanov.tonkoslovie.oauth.CustomTokenResponseConverter;
import ru.romanov.tonkoslovie.security.JwtAuthenticationProvider;
import ru.romanov.tonkoslovie.security.JwtService;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2AuthenticationSuccessHandler oauthAuthenticationSuccessHandler;
    private final CustomOidcUserService oidcUserService;
    private final CustomOAuth2UserService oauth2UserService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/login",
                        "/api/user/registration/**",
                        "/api/oauth/**",
                        "/actuator/health").permitAll()
                .requestMatchers("/api/content/exercises").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/content/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/content/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/content/**").hasRole("ADMIN")
                .requestMatchers("/api/user/users").hasRole("ADMIN")
                .requestMatchers("/api/user").hasRole("ADMIN")
                .requestMatchers("/api/user/update").hasRole("ADMIN")
                .anyRequest().authenticated());

        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilter(headerAuthenticationFilter(authenticationManager()));

        http.oauth2Login(oauth -> oauth
                .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
                        .baseUri("/api/oauth/login"))
                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
                        .baseUri("/api/oauth/login/callback/*"))
                .tokenEndpoint(tokenEndpointConfig -> tokenEndpointConfig
                        .accessTokenResponseClient(accessTokenResponseClient()))
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                        .userService(oauth2UserService)
                        .oidcUserService(oidcUserService))
                .successHandler(oauthAuthenticationSuccessHandler));

        return http.build();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager() {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth, JwtAuthenticationProvider authProvider) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public JwtAuthenticationProvider authProvider(JwtService jwtService) {
        return new JwtAuthenticationProvider(jwtService);
    }

    private RequestHeaderAuthenticationFilter headerAuthenticationFilter(AuthenticationManager authenticationManager) {
        RequestHeaderAuthenticationFilter headerAuthenticationFilter = new RequestHeaderAuthenticationFilter();
        headerAuthenticationFilter.setPrincipalRequestHeader("Authorization");
        headerAuthenticationFilter.setExceptionIfHeaderMissing(false);
        headerAuthenticationFilter.setAuthenticationManager(authenticationManager);
        headerAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            request.getSession(true);
            response.sendError(403);
        });
        headerAuthenticationFilter.afterPropertiesSet();
        return headerAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenConverter.setAccessTokenResponseConverter(new CustomTokenResponseConverter());

        RestTemplate restTemplate = new RestTemplate(List.of(new FormHttpMessageConverter(), tokenConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(new OAuth2AuthorizationCodeGrantRequestEntityConverter());
        client.setRestOperations(restTemplate);
        return client;
    }

}