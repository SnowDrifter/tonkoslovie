package ru.romanov.tonkoslovie.user.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public enum Role implements GrantedAuthority {

    ROLE_USER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
