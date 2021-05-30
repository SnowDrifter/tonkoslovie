package ru.romanov.tonkoslovie.mail.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.mail.service.MailService;
import ru.romanov.tonkoslovie.model.mail.MailMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailConsumer {

    private final MailService mailService;

    @KafkaListener(topics = "mail-topic")
    public void consumeMail(MailMessage message) {
        log.debug("Message: {}", message);

        try {
            mailService.sendMail(message);
        } catch (Exception e) {
            log.error("Cannot send message {}, Error: {}: {}", message, e.getClass().getSimpleName(), e.getMessage());
        }
    }

}
