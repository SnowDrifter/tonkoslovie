package ru.romanov.tonkoslovie.mail.entity;

import lombok.Data;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class EmailVerification {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private User user;
    private Date expiryDate;
    private String token;

}
