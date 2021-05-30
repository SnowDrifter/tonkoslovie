package ru.romanov.tonkoslovie.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.partitions:10}")
    private int partitions;

    @Value("${kafka.replication:1}")
    private int replication;

    @Bean
    public NewTopic mailTopic() {
        return TopicBuilder.name("mail-topic")
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

}
