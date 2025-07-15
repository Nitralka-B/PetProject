package com.bank.transfer.config;

import com.bank.transfer.DTO.AccountDetailsDto;
import com.bank.transfer.DTO.IncomingTransferDto;
import com.bank.transfer.Util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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

@Configuration
@Slf4j
public class KafkaConsumerConfig {
    private Map<String, Object> baseConsumerProps() {
        final Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transfer-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // НЕ указываем VALUE_DESERIALIZER_CLASS_CONFIG здесь, задаём отдельно
        return props;
    }
    @Bean
    public CommonErrorHandler defaultErrorHandler() {
        return new DefaultErrorHandler(
                (record, ex) -> log.error("Ошибка обработки после повторных попыток для топика {}: {}",
                        record.topic(),
                        record.value(), ex),
                new FixedBackOff(Constants.RETRY_INTERVAL_MS, Constants.MAX_RETRY_ATTEMPTS)
        );
    }
    // Фабрика для AccountDetailsDto
    @Bean
    public ConsumerFactory<String, AccountDetailsDto> accountDetailsConsumerFactory() {
        final Map<String, Object> props = baseConsumerProps();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, AccountDetailsDto.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, Constants.TRUST_ALL_PACKAGES);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean(name = "accountDetailsKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, AccountDetailsDto>
            accountDetailsKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, AccountDetailsDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(accountDetailsConsumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }


    @Bean
    public ConsumerFactory<String, IncomingTransferDto> incomingTransferConsumerFactory() {
        final Map<String, Object> props = baseConsumerProps();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, IncomingTransferDto.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.bank.transfer.DTO");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }


    @Bean(name = "incomingTransferKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, IncomingTransferDto>
            incomingTransferKafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, IncomingTransferDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(incomingTransferConsumerFactory());
        factory.setCommonErrorHandler(defaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return factory;
    }


}
