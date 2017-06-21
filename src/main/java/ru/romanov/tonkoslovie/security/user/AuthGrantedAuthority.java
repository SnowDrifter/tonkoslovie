package ru.romanov.tonkoslovie.security.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;


public class AuthGrantedAuthority implements GrantedAuthority {

    private String authority;

    public AuthGrantedAuthority() {
    }

    public AuthGrantedAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

}