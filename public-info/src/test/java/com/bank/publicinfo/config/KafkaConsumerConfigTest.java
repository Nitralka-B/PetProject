package com.bank.publicinfo.config;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.testutil.TestConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для {@link KafkaConsumerConfig}.
 * Проверяют конфигурацию Kafka Consumer:
 */
@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

    private KafkaConsumerConfig kafkaConsumerConfig;

    /**
     * Инициализация тестового окружения перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        kafkaConsumerConfig = new KafkaConsumerConfig();
        ReflectionTestUtils.setField(kafkaConsumerConfig,
                TestConstants.BOOTSTRAP_SERVERS_FIELD,
                TestConstants.KAFKA_BOOTSTRAP_SERVERS);
    }

    /**
     * Проверяет наличие основных свойств потребителя.
     */
    @Test
    void shouldContainCorrectCommonConsumerProperties() {
        Map<String, Object> props = getCommonConsumerProperties();
        assertCommonConsumerProperties(props);
    }

    /**
     * Проверяет конфигурацию фабрики для удаления банков.
     */
    @Test
    void bankDeleteConsumerFactory_ShouldConfigureCorrectDeserializers() {
        ConsumerFactory<String, String> factory = kafkaConsumerConfig.bankDeleteConsumerFactory();
        assertConsumerFactory(factory);
    }

    /**
     * Проверяет конфигурацию фабрики для банковских реквизитов.
     */
    @Test
    void bankDetailsConsumerFactory_ShouldUseJsonDeserializer() {
        ConsumerFactory<String, BankDetailsDto> factory = kafkaConsumerConfig.bankDetailsConsumerFactory();
        assertNotNull(factory, TestConstants.CONSUMER_FACTORY_SHOULD_NOT_BE_NULL);
    }

    /**
     * Проверяет режим подтверждения для удаления банков.
     */
    @Test
    void bankDeleteKafkaListenerContainerFactory_ShouldHaveManualAckMode() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                kafkaConsumerConfig.bankDeleteKafkaListenerContainerFactory();

        assertEquals(
                TestConstants.KAFKA_ACK_MODE,
                factory.getContainerProperties().getAckMode().name(),
                TestConstants.ACK_MODE_SHOULD_MATCH
        );
    }

    /**
     * Проверяет настройку обработчика ошибок по умолчанию.
     */
    @Test
    void defaultErrorHandler_ShouldBeConfigured() {
        CommonErrorHandler handler = kafkaConsumerConfig.defaultErrorHandler();
        assertNotNull(handler, TestConstants.DEFAULT_ERROR_HANDLER_SHOULD_BE_CONFIGURED);
    }

    /**
     * Проверяет конфигурацию фабрики для получения банков.
     */
    @Test
    void bankGetConsumerFactory_ShouldConfigureCorrectDeserializers() {
        ConsumerFactory<String, String> factory = kafkaConsumerConfig.bankGetConsumerFactory();
        assertConsumerFactory(factory);
    }

    /**
     * Проверяет конфигурацию фабрики слушателей для получения банков.
     */
    @Test
    void bankGetKafkaListenerContainerFactory_ShouldBeConfigured() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                kafkaConsumerConfig.bankGetKafkaListenerContainerFactory();
        assertNotNull(factory, TestConstants.BANK_GET_LISTENER_FACTORY_SHOULD_BE_CONFIGURED);
    }

    /**
     * Проверяет конфигурацию фабрики слушателей для банковских реквизитов.
     */
    @Test
    void bankDetailsKafkaListenerContainerFactory_ShouldBeConfigured() {
        ConcurrentKafkaListenerContainerFactory<String, BankDetailsDto> factory =
                kafkaConsumerConfig.bankDetailsKafkaListenerContainerFactory();
        assertNotNull(factory, TestConstants.BANK_DETAILS_LISTENER_FACTORY_SHOULD_BE_CONFIGURED);
    }

    /**
     * Проверяет дополнительные свойства потребителя.
     */
    @Test
    void commonConsumerProps_ShouldContainAllProperties() {
        Map<String, Object> props = getCommonConsumerProperties();

        assertEquals(
                TestConstants.KAFKA_MAX_POLL_INTERVAL_MS,
                props.get(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG),
                TestConstants.MAX_POLL_INTERVAL_SHOULD_MATCH
        );
        assertEquals(
                TestConstants.KAFKA_SESSION_TIMEOUT_MS,
                props.get(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG),
                TestConstants.SESSION_TIMEOUT_SHOULD_MATCH
        );
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCommonConsumerProperties() {
        return (Map<String, Object>) ReflectionTestUtils.invokeMethod(
                kafkaConsumerConfig,
                TestConstants.COMMON_CONSUMER_PROPS_METHOD
        );
    }

    private void assertCommonConsumerProperties(Map<String, Object> props) {
        assertEquals(
                TestConstants.KAFKA_BOOTSTRAP_SERVERS,
                props.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG),
                TestConstants.BOOTSTRAP_SERVERS_SHOULD_MATCH
        );
        assertEquals(
                TestConstants.KAFKA_GROUP_ID,
                props.get(ConsumerConfig.GROUP_ID_CONFIG),
                TestConstants.GROUP_ID_SHOULD_MATCH
        );
        assertEquals(
                TestConstants.KAFKA_AUTO_OFFSET_RESET,
                props.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG),
                TestConstants.AUTO_OFFSET_RESET_SHOULD_MATCH
        );
        assertFalse(
                (Boolean) props.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG),
                TestConstants.AUTO_COMMIT_SHOULD_BE_DISABLED
        );
    }

    private void assertConsumerFactory(ConsumerFactory<String, String> factory) {
        assertNotNull(factory, TestConstants.CONSUMER_FACTORY_SHOULD_NOT_BE_NULL);
        assertDoesNotThrow(() -> factory.createConsumer().close(),
                TestConstants.CONSUMER_CREATION_SHOULD_NOT_THROW);
    }
}
