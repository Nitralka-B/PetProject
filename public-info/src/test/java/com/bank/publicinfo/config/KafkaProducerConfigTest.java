package com.bank.publicinfo.config;

import com.bank.publicinfo.testutil.TestConstants;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Тестовый класс для проверки конфигурации Kafka Producer в {@link KafkaProducerConfig}.
 */
@ExtendWith(MockitoExtension.class)
class KafkaProducerConfigTest {

    /**
     * Проверяет корректность конфигурации фабрики продюсера:
     */
    @Test
    void producerFactory_ShouldBeConfiguredCorrectly() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        ReflectionTestUtils.setField(config, TestConstants.BOOTSTRAP_SERVERS_FIELD, TestConstants.KAFKA_BOOTSTRAP_SERVERS);

        ProducerFactory<String, Object> factory = config.producerFactory();
        Map<String, Object> configProps = factory.getConfigurationProperties();

        assertNotNull(factory, TestConstants.PRODUCER_FACTORY_SHOULD_NOT_BE_NULL);
        assertEquals(TestConstants.KAFKA_BOOTSTRAP_SERVERS,
                configProps.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG),
                TestConstants.BOOTSTRAP_SERVERS_SHOULD_MATCH);
        assertEquals(StringSerializer.class,
                configProps.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG),
                TestConstants.KEY_SERIALIZER_SHOULD_BE_STRING);
        assertEquals(JsonSerializer.class,
                configProps.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG),
                TestConstants.VALUE_SERIALIZER_SHOULD_BE_JSON);
        assertTrue(configProps.containsKey(JsonSerializer.TYPE_MAPPINGS),
                TestConstants.TYPE_MAPPINGS_SHOULD_BE_CONFIGURED);
    }

    /**
     * Проверяет, что KafkaTemplate создается с использованием переопределенной фабрики продюсера.
     */
    @Test
    void kafkaTemplate_ShouldUseProducerFactory() {
        KafkaProducerConfig config = new KafkaProducerConfig() {
            @Override
            public ProducerFactory<String, Object> producerFactory() {
                return mock(ProducerFactory.class);
            }
        };

        KafkaTemplate<String, Object> template = config.kafkaTemplate();

        assertNotNull(template, TestConstants.KAFKA_TEMPLATE_SHOULD_NOT_BE_NULL);
        assertNotNull(template.getProducerFactory(), TestConstants.PRODUCER_FACTORY_SHOULD_NOT_BE_NULL);
    }

    /**
     * Проверяет создание KafkaTemplate с фабрикой продюсера по умолчанию:
     */
    @Test
    void kafkaTemplate_ShouldBeCreatedWithDefaultFactory() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        ReflectionTestUtils.setField(config, TestConstants.BOOTSTRAP_SERVERS_FIELD, TestConstants.KAFKA_BOOTSTRAP_SERVERS);

        KafkaTemplate<String, Object> template = config.kafkaTemplate();

        assertNotNull(template, TestConstants.KAFKA_TEMPLATE_SHOULD_NOT_BE_NULL);
        assertNotNull(template.getProducerFactory(), TestConstants.PRODUCER_FACTORY_SHOULD_NOT_BE_NULL);
    }
}
