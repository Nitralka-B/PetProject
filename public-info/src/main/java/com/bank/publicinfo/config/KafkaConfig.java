package com.bank.publicinfo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Конфигурация Kafka топиков.
 * Создает и настраивает топики для работы с событиями банковских операций.
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.partitions:3}")
    private int partitions;

    @Value("${spring.kafka.topics.replicas:1}")
    private int replicas;

    @Value("${spring.kafka.topics.retention.ms.default:604800000}") // 7 дней
    private long defaultRetentionMs;

    @Value("${spring.kafka.topics.retention.ms.short:3600000}") // 1 час
    private long shortRetentionMs;

    @Value("${spring.kafka.topics.bank.create:public-info.bank.create}")
    private String bankCreateTopic;

    @Value("${spring.kafka.topics.bank.update:public-info.bank.update}")
    private String bankUpdateTopic;

    @Value("${spring.kafka.topics.bank.delete:public-info.bank.delete}")
    private String bankDeleteTopic;

    @Value("${spring.kafka.topics.bank.get:public-info.bank.get}")
    private String bankGetTopic;

    @Value("${spring.kafka.topics.bank.response:public-info.bank.response}")
    private String bankResponseTopic;

    @Bean
    public NewTopic bankCreateTopic() {
        return buildBankTopic(bankCreateTopic);
    }

    @Bean
    public NewTopic bankUpdateTopic() {
        return buildBankTopic(bankUpdateTopic);
    }

    @Bean
    public NewTopic bankDeleteTopic() {
        return buildBankTopic(bankDeleteTopic);
    }

    @Bean
    public NewTopic bankGetTopic() {
        return buildBankTopic(bankGetTopic);
    }

    @Bean
    public NewTopic bankResponseTopic() {
        return TopicBuilder.name(bankResponseTopic)
                .partitions(partitions)
                .replicas(replicas)
                .config("retention.ms", String.valueOf(shortRetentionMs))
                .build();
    }

    private NewTopic buildBankTopic(String name) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicas)
                .config("retention.ms", String.valueOf(defaultRetentionMs))
                .build();
    }
}
