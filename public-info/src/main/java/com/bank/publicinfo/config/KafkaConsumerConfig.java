package com.bank.publicinfo.config;

import com.bank.publicinfo.dto.BankDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки Kafka Consumer'ов.
 * Содержит конфигурацию фабрик потребителей для различных типов сообщений.
 */
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:29092}")
    private String bootstrapServers;

    /**
     * Создает общие настройки для всех потребителей Kafka.
     * @return Map с общими настройками потребителей
     */
    private Map<String, Object> commonConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "public-info-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
        return props;
    }

    /**
     * Создает обработчик ошибок по умолчанию для Kafka Consumer'ов.
     * @return обработчик ошибок с фиксированным интервалом повторных попыток
     */
    @Bean
    public CommonErrorHandler defaultErrorHandler() {
        return new DefaultErrorHandler(
                (record, ex) -> log.error("Ошибка обработки после повторных попыток для топика {}: {}", record.topic(), record.value(), ex),
                new FixedBackOff(1000L, 3L)
        );
    }

    /**
     * Создает фабрику потребителей для операций DELETE.
     * @return фабрика потребителей для строковых сообщений
     */
    @Bean
    public ConsumerFactory<String, String> bankDeleteConsumerFactory() {
        Map<String, Object> props = commonConsumerProps();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.publicinfo.dto");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new StringDeserializer())
        );
    }

    /**
     * Создает фабрику контейнеров слушателей для операций DELETE.
     * @return фабрика контейнеров с ручным подтверждением сообщений
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> bankDeleteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bankDeleteConsumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    /**
     * Создает фабрику потребителей для операций GET.
     * @return фабрика потребителей для строковых сообщений
     */
    @Bean
    public ConsumerFactory<String, String> bankGetConsumerFactory() {
        Map<String, Object> props = commonConsumerProps();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.publicinfo.dto");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new StringDeserializer())
        );
    }

    /**
     * Создает фабрику контейнеров слушателей для операций GET.
     * @return фабрика контейнеров слушателей
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> bankGetKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bankGetConsumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        return factory;
    }

    /**
     * Создает фабрику потребителей для сообщений с банковскими реквизитами.
     * @return фабрика потребителей для BankDetailsDto
     */
    @Bean
    public ConsumerFactory<String, BankDetailsDto> bankDetailsConsumerFactory() {
        Map<String, Object> props = commonConsumerProps();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.publicinfo.dto");

        JsonDeserializer<BankDetailsDto> deserializer = new JsonDeserializer<>(BankDetailsDto.class);
        deserializer.addTrustedPackages("com.bank.publicinfo.dto");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(deserializer)
        );
    }

    /**
     * Создает фабрику контейнеров слушателей для BankDetailsDto.
     * @return фабрика контейнеров слушателей для BankDetailsDto
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BankDetailsDto> bankDetailsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BankDetailsDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bankDetailsConsumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        return factory;
    }
}