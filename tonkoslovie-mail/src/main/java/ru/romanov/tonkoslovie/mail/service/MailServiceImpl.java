package ru.romanov.tonkoslovie.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.romanov.tonkoslovie.model.mail.MailMessage;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendMail(MailMessage mailMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject("Подтверждение регистрации");
            message.setFrom("tonkoslovie@no-reply", "tonkoslovie@no-reply");
            message.setTo(mailMessage.getAddress());

            Context context = new Context();
            context.setVariable("link", mailMessage.getResponseHost() + "/api/user/registration/confirm?token=" + mailMessage.getToken());
            if (StringUtils.hasText(mailMessage.getUsername())) {
                context.setVariable("displayName", mailMessage.getUsername());
            }

            String htmlContent = templateEngine.process("confirm-registration.html", context);
            message.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("Sending mail error, message: {}, email: {}", e.getMessage(), mailMessage.getAddress());
        }
    }

}
