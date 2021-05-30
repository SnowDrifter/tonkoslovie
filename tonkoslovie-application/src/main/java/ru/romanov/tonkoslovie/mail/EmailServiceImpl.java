package ru.romanov.tonkoslovie.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.mail.kafka.MailKafkaProducer;
import ru.romanov.tonkoslovie.model.mail.MailMessage;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${app.host}")
    private String host;

    private final EmailVerificationRepository emailVerificationRepository;
    private final MailKafkaProducer mailKafkaProducer;

    @Override
    public void sendVerification(User user) {
        UUID token = UUID.randomUUID();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpirationDate(calculateExpirationDate());
        emailVerificationRepository.save(verification);

        MailMessage mailMessage = new MailMessage();
        mailMessage.setUsername(user.getUsername());
        mailMessage.setAddress(user.getEmail());
        mailMessage.setToken(token.toString());
        mailMessage.setResponseHost(host);
        mailKafkaProducer.sendMessage(mailMessage);
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        return calendar.getTime();
    }
}
