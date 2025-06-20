package com.bank.profile.Configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.kafka.config.TopicBuilder.name;

/**
 * Конфигурационный класс для kafka(создание топиков)
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.topics.profile-create}")
    private String createTopic;

    @Value("${spring.kafka.topics.profile-delete}")
    private String deleteTopic;

    @Value("${spring.kafka.topics.profile-update}")
    private String updateTopic;

    @Value("${spring.kafka.topics.profile-get}")
    private String getTopic;

    @Value("${spring.kafka.topics.profile-requestget}")
    private String requestGetTopic;

    @Value("${spring.kafka.topics.profile-getPrincipal}")
    private String getPrincipalTopic;

    @Value("${spring.kafka.topics.profile-errors}")
    private String errorsTopic;

    @Value("$spring.kafka.topics.partitions")
    private int partitionsCount;

    @Value("$spring.kafka.topics.replicas")
    private int replicasCount;

    private NewTopic buildTopic(String topicName) {
        return name(topicName)
                .partitions(partitionsCount)
                .replicas(replicasCount)
                .build();
    }

    @Bean
    public NewTopic profileCreateTopic() {
        return buildTopic(createTopic);
    }

    @Bean
    public NewTopic profileUpdateTopic() {
        return buildTopic(updateTopic);
    }

    @Bean
    public NewTopic profileDeleteTopic() {
        return buildTopic(deleteTopic);
    }

    @Bean
    public NewTopic profileGetTopic() {
        return buildTopic(getTopic);
    }

    @Bean
    public NewTopic profileRequestGetTopic() {
        return buildTopic(requestGetTopic);
    }

    @Bean
    public NewTopic profileGetPrincipalTopic() {
        return buildTopic(getPrincipalTopic);
    }

    @Bean
    public NewTopic profileErrorTopic() {
        return buildTopic(errorsTopic);
    }
}
