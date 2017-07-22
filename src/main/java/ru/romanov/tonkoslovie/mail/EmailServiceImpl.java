package ru.romanov.tonkoslovie.mail;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${mail.host}")
    private String host;
    private final it.ozimov.springboot.mail.service.EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailServiceImpl(it.ozimov.springboot.mail.service.EmailService emailService, EmailVerificationRepository emailVerificationRepository) {
        this.emailService = emailService;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Override
    public void sendVerification(User user) throws UnsupportedEncodingException, AddressException, CannotSendEmailException {
        String token = UUID.randomUUID().toString();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiryDate(new Date());
        emailVerificationRepository.save(verification);

        Email email = DefaultEmail.builder()
                .from(new InternetAddress("test@no-reply", "Test"))
                .to(Lists.newArrayList(new InternetAddress(user.getEmail())))
                .subject("Подтверждение регистрации")
                .body("")
                .encoding("UTF-8").build();

        final Map<String, Object> emailData = new HashMap<>();
        emailData.put("displayName", user.getUsername());
        emailData.put("link", host + "/confirmRegistration?token=" + token);

        emailService.send(email, "confirmRegistration.html", emailData);
    }
}
