package ru.romanov.tonkoslovie.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity(name = "persistent_logins")
public class PersistentLogins {

    @NotNull
    private String username;
    @Id
    private String series;
    @NotNull
    private String token;
    @NotNull
    private Date last_used;

}