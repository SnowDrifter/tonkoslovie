package ru.romanov.tonkoslovie.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "\"user\"",
        indexes = {
                @Index(name = "emailIndex", columnList = "email")
        }
)
@JsonIgnoreProperties({"password" , "token", "authorities", "accountNonLocked", "credentialsNonExpired", "accountNonExpired"})
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    @CollectionTable(name = "roles")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();
    private String username;
    private String password;
    private Date creationDate;
    private String email;
    private String firstName;
    private String lastName;
    @Column(columnDefinition = "boolean default false")
    private boolean enabled;
    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
