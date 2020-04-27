package ru.romanov.tonkoslovie.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.romanov.tonkoslovie.mail.entity.EmailVerification;
import ru.romanov.tonkoslovie.user.entity.User;

import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${app.host}")
    private String host;
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void sendVerification(User user) {
        UUID token = UUID.randomUUID();

        EmailVerification verification = new EmailVerification();
        verification.setUser(user);
        verification.setToken(token);
        verification.setExpiryDate(new Date());
        emailVerificationRepository.save(verification);


        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject("Подтверждение регистрации");
            message.setFrom("tonkoslovie@no-reply", "tonkoslovie@no-reply");
            message.setTo(user.getEmail());

            Context context = new Context();
            context.setVariable("link", host + "/api/user/confirmRegistration?token=" + token);
            if (StringUtils.hasText(user.getUsername())) {
                context.setVariable("displayName", user.getUsername());
            }

            String htmlContent = templateEngine.process("confirmRegistration.html", context);
            message.setText(htmlContent, true);

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Sending email error, message: {}, email: {}", e.getMessage(), user.getEmail());
        }
    }
}
