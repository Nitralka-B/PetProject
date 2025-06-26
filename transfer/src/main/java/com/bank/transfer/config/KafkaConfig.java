package com.bank.transfer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import java.util.List;


@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.partitions:1}")
    private static int partitions;

    @Value("${kafka.replication-factor:1}")
    private static short replicationFactor;

    @Bean
    public List<NewTopic> newTopics() {
        return List.of(
                new NewTopic("transfer.phone", partitions, replicationFactor),
                new NewTopic("transfer.account", partitions, replicationFactor),
                new NewTopic("transfer.card", partitions, replicationFactor),
                new NewTopic("audit.events", partitions, replicationFactor)
        );
    }
}
