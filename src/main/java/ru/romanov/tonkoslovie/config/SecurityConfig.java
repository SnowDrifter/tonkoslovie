package ru.romanov.tonkoslovie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.romanov.tonkoslovie.user.UserService;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "ru.romanov")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll().anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll().permitAll().failureUrl("/login?error=true")
                .and()
                .logout().permitAll().logoutSuccessUrl("/")
                .and()
                .csrf().disable();

        http.userDetailsService(userService);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}
