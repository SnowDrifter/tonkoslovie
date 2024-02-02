package ru.romanov.tonkoslovie.user.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.romanov.tonkoslovie.utils.UserUtil.ROLES_DELIMITER;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "\"user\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_sequence", allocationSize = 1)
    private Long id;
    @Convert(converter = RoleAttributeConverter.class)
    private Set<Role> roles = new HashSet<>();
    private String username;
    private String password;
    private Date creationDate;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    @Type(JsonBinaryType.class)
    private SocialMedia socialMedia;

    public User(long id, String roles) {
        this.id = id;

        this.roles = Stream.of(roles.split(ROLES_DELIMITER))
                .map(String::trim)
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
