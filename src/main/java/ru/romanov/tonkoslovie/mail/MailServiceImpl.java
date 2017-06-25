package ru.romanov.tonkoslovie.mail;

import com.google.common.collect.Lists;
import it.ozimov.springboot.templating.mail.model.Email;
import it.ozimov.springboot.templating.mail.model.impl.EmailImpl;
import it.ozimov.springboot.templating.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.host}")
    private String host;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Autowired
    public MailServiceImpl(EmailService emailService, EmailVerificationRepository emailVerificationRepository) {
        this.emailService = emailService;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Override
    public void sendVerification(User user) throws UnsupportedEncodingException, AddressException {
        String token = UUID.randomUUID().toString();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiryDate(new Date());
        emailVerificationRepository.save(verification);

        Email email = EmailImpl.builder()
                .from(new InternetAddress("test@no-reply", "Test"))
                .to(Lists.newArrayList(new InternetAddress(user.getEmail())))
                .subject("Подтверждение регистрации")
                .body(host + "/confirmRegistration?token=" + token)
                .encoding(Charset.forName("UTF-8")).build();

        emailService.send(email);
    }
}
