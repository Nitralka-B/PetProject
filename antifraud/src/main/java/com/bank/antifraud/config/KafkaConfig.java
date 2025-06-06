package com.bank.antifraud.config;

import com.bank.antifraud.util.KafkaConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Класс для создания новых топиков и партиций
 */

@Configuration
public class KafkaConfig {
    @Value("${kafka.partitions:1}")
    private static int partitions;

    @Value("${kafka.replication-factor:1}")
    private static short replicationFactor;

    @Bean
    public List<NewTopic> newTopics() {
        return List.of(
                new NewTopic(KafkaConstants.SUSPICIOUS_CREATE, partitions, replicationFactor),
                new NewTopic(KafkaConstants.SUSPICIOUS_UPDATE, partitions, replicationFactor),
                new NewTopic(KafkaConstants.SUSPICIOUS_DELETE, partitions, replicationFactor),
                new NewTopic(KafkaConstants.SUSPICIOUS_GET, partitions, replicationFactor),
                new NewTopic(KafkaConstants.AUDIT_EVENTS, partitions, replicationFactor)
        );
    }
}
