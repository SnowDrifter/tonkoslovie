package ru.romanov.tonkoslovie.mail.entity;

import lombok.Data;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_id_generator")
    @SequenceGenerator(name = "email_verification_id_generator", sequenceName = "email_verification_id_sequence", allocationSize = 1)
    private Long id;

    @OneToOne
    private User user;

    private Date expirationDate;

    private UUID token;

}
