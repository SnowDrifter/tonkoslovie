package ru.romanov.tonkoslovie.mail.entity;

import lombok.Data;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class EmailVerification {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private User user;
    private Date expirationDate;
    private UUID token;

}
