package ru.romanov.tonkoslovie.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.CorsFilter;
import ru.romanov.tonkoslovie.oauth.ClientResources;
import ru.romanov.tonkoslovie.oauth.OauthAuthenticationSuccessHandler;
import ru.romanov.tonkoslovie.oauth.extractor.FacebookPrincipalExtractor;
import ru.romanov.tonkoslovie.oauth.extractor.GooglePrincipalExtractor;
import ru.romanov.tonkoslovie.oauth.extractor.VkPrincipalExtractor;
import ru.romanov.tonkoslovie.oauth.vk.VkCustomFilter;
import ru.romanov.tonkoslovie.oauth.vk.VkUserInfoTokenService;
import ru.romanov.tonkoslovie.security.JwtAuthenticationProvider;
import ru.romanov.tonkoslovie.security.JwtService;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableOAuth2Client
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2ClientContext oauth2ClientContext;
    private final GooglePrincipalExtractor googlePrincipalExtractor;
    private final FacebookPrincipalExtractor facebookPrincipalExtractor;
    private final VkPrincipalExtractor vkPrincipalExtractor;
    private final OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/user/login",
                        "/api/user/registration",
                        "/api/user/confirmRegistration",
                        "/api/oauth/**",
                        "/actuator/health").permitAll()
                .antMatchers(HttpMethod.GET, "/api/content/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/content/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/content/**").hasRole("ADMIN")
                .antMatchers("/api/media/**").hasRole("ADMIN")
                .antMatchers("/api/user/users").hasRole("ADMIN")
                .antMatchers("/api/user").hasRole("ADMIN")
                .antMatchers("/api/user/update").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.csrf().disable();

        http.addFilter(headerAuthenticationFilter(authenticationManager()));
        http.addFilter(corsFilter());
        http.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
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
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setOrder(-100);
        return registration;
    }

    @Bean
    @ConfigurationProperties("vk")
    public ClientResources vk() {
        return new ClientResources(vkPrincipalExtractor);
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources(googlePrincipalExtractor);
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources(facebookPrincipalExtractor);
    }

    private Filter ssoFilter() {
        List<Filter> filters = new ArrayList<>();
        filters.add(vkSsoFilter(vk(), "/api/oauth/login/vk"));
        filters.add(ssoFilter(google(), "/api/oauth/login/google"));
        filters.add(ssoFilter(facebook(), "/api/oauth/login/facebook"));

        CompositeFilter filter = new CompositeFilter();
        filter.setFilters(filters);
        return filter;
    }

    private Filter vkSsoFilter(ClientResources client, String path) {
        VkCustomFilter filter = new VkCustomFilter(path);

        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        VkUserInfoTokenService tokenServices = new VkUserInfoTokenService(client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setPrincipalExtractor(client.getPrincipalExtractor());
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(oauthAuthenticationSuccessHandler);

        return filter;
    }

    private Filter ssoFilter(ClientResources client, String path) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);

        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId());
        tokenServices.setRestTemplate(template);
        tokenServices.setPrincipalExtractor(client.getPrincipalExtractor());
        filter.setTokenServices(tokenServices);
        filter.setAuthenticationSuccessHandler(oauthAuthenticationSuccessHandler);

        return filter;
    }
}