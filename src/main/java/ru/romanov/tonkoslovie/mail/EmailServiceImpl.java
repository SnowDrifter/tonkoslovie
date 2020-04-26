package ru.romanov.tonkoslovie.mail;

import com.google.common.collect.Lists;
import it.ozimov.springboot.mail.model.Email;
import it.ozimov.springboot.mail.model.defaultimpl.DefaultEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.InternetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${app.host}")
    private String host;
    private final it.ozimov.springboot.mail.service.EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public EmailServiceImpl(it.ozimov.springboot.mail.service.EmailService emailService, EmailVerificationRepository emailVerificationRepository) {
        this.emailService = emailService;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Override
    public void sendVerification(User user) {
        UUID token = UUID.randomUUID();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiryDate(new Date());
        emailVerificationRepository.save(verification);

        try {
            Email email = DefaultEmail.builder()
                    .from(new InternetAddress("tonkoslovie@no-reply"))
                    .to(Lists.newArrayList(new InternetAddress(user.getEmail())))
                    .subject("Подтверждение регистрации")
                    .body("")
                    .encoding("UTF-8").build();

            final Map<String, Object> emailData = new HashMap<>();
            emailData.put("link", host + "/api/user/confirmRegistration?token=" + token);

            if (StringUtils.hasText(user.getUsername())) {
                emailData.put("displayName", user.getUsername());
            } else if (StringUtils.hasText(user.getFirstName())) {
                emailData.put("displayName", user.getFirstName());
            }

            emailService.send(email, "confirmRegistration.html", emailData);
        } catch (Exception e) {
            log.error("Sending email error, message: {}, email: {}", e.getMessage(), user.getEmail());
        }
    }
}
