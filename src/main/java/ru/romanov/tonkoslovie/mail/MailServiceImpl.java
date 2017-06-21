package ru.romanov.tonkoslovie.mail;

import com.google.common.collect.Lists;
import it.ozimov.springboot.templating.mail.model.Email;
import it.ozimov.springboot.templating.mail.model.impl.EmailImpl;
import it.ozimov.springboot.templating.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@Service
public class MailServiceImpl implements MailService {

    @Value("${mail.host}")
    private String host;
    private final EmailService emailService;

    @Autowired
    public MailServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void sendVerification(String toAddress, String token) {
        Email email = null;
        try {
            email = EmailImpl.builder()
                    .from(new InternetAddress("test@no-reply", "Test"))
                    .to(Lists.newArrayList(new InternetAddress(toAddress)))
                    .subject("Подтверждение регистрации")
                    .body(host + "/confirmRegistration?token=" + token)
                    .encoding(Charset.forName("UTF-8")).build();
        } catch (AddressException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        emailService.send(email);
    }
}
