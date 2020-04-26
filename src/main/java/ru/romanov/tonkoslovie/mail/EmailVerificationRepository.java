package ru.romanov.tonkoslovie.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;

import java.util.UUID;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    EmailVerification findByToken(UUID token);

}
