package ru.romanov.tonkoslovie.mail.entity;

import lombok.Getter;
import lombok.Setter;
import ru.romanov.tonkoslovie.user.entity.User;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
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
