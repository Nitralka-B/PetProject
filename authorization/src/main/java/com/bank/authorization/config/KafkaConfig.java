package com.bank.authorization.config;


import com.bank.authorization.exception.ValidationException;
import com.bank.authorization.kafka.enumTopic.KafkaTopic;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;


/**
 * Конфигурация Kafka-топиков для работы с пользователями.
 * Автоматически создает топики при старте приложения с заданными параметрами:
 * - 3 партиции для каждого топика.
 * - 1 реплика.
 * Названия топиков берутся из конфигурации (application.properties).
 */
@Configuration
@Slf4j
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private static final int MAX_ATTEMPTS = 3;

    @Value("${spring.kafka.topics.user-create}")
    private String userCreateTopic;

    @Value("${spring.kafka.topics.user-delete}")
    private String userDeleteTopic;

    @Value("${spring.kafka.topics.user-update}")
    private String userUpdateTopic;

    @Value("${spring.kafka.topics.user-get}")
    private String userGetTopic;

    @Value("${spring.kafka.partitions}")
    private int partitions;

    @Value("${spring.kafka.replicas}")
    private short replication;

    @Bean
    public CommonErrorHandler commonErrorHandler(KafkaTemplate<String, Object> template) {
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(template),
                new FixedBackOff(1000, MAX_ATTEMPTS)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory(
            CommonErrorHandler commonErrorHandler
    ) {
        final ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }

    @PostConstruct
    public void init() {
        KafkaTopic.USER_CREATE.setTopicName(userCreateTopic);
        KafkaTopic.USER_DELETE.setTopicName(userDeleteTopic);
        KafkaTopic.USER_UPDATE.setTopicName(userUpdateTopic);
        KafkaTopic.USER_GET.setTopicName(userGetTopic);

        log.info("Initialized Kafka topics: {}", Arrays.toString(KafkaTopic.values()));
    }

    private NewTopic buildTopic(String topicName) {
        if (topicName == null || topicName.trim().isEmpty()) {
            throw new ValidationException("EMPTY_TOPIC", "Название топика не может быть пустым или null");
        }
        log.debug("Building topic: {} (partitions={}, replicas={})", topicName, partitions, replication);
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    @Bean
    public NewTopic userCreateTopic() {
        log.debug("Создание топика для создания пользователей: {}", KafkaTopic.USER_CREATE);
        return buildTopic(KafkaTopic.USER_CREATE.getTopicName());
    }

    @Bean
    public NewTopic userDeleteTopic() {
        log.debug("Создание топика для удаления пользователей: {}", KafkaTopic.USER_DELETE);
        return buildTopic(KafkaTopic.USER_DELETE.getTopicName());
    }

    @Bean
    public NewTopic userUpdateTopic() {
        log.debug("Создание топика для обновления пользователей: {}", KafkaTopic.USER_UPDATE);
        return buildTopic(KafkaTopic.USER_UPDATE.getTopicName());
    }

    @Bean
    public NewTopic userGetTopic() {
        log.debug("Создание топика для получения пользователей: {}", KafkaTopic.USER_GET);
        return buildTopic(KafkaTopic.USER_GET.getTopicName());
    }

}
