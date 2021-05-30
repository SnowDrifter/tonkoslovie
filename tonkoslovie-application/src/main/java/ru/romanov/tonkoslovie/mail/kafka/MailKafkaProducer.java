package ru.romanov.tonkoslovie.mail.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.romanov.tonkoslovie.model.mail.MailMessage;

@Component
@RequiredArgsConstructor
public class MailKafkaProducer {

    private final KafkaTemplate<String, MailMessage> kafkaTemplate;

    public void sendMessage(MailMessage message) {
        kafkaTemplate.send("mail-topic", message);
    }

}
