package ru.romanov.tonkoslovie.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.romanov.tonkoslovie.user.entity.Role;

import java.util.Date;
import java.util.Set;

@Data
public class UserDto {

    private Long id;
    private Set<Role> roles;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Date creationDate;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;

}
