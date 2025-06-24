package com.bank.publicinfo.config;

import com.bank.publicinfo.testutil.TestConstants;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тестовый класс для проверки конфигурации Kafka-топиков в {@link KafkaConfig}.
 */
@ExtendWith(MockitoExtension.class)
class KafkaConfigTest {

    @InjectMocks
    private KafkaConfig kafkaConfig;

    /**
     * Тестирует конфигурацию топика для создания банка.
     */
    @Test
    void testBankCreateTopicConfiguration() {

        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.DEFAULT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_DEFAULT_RETENTION_MS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.BANK_CREATE_TOPIC_FIELD,
                TestConstants.BANK_CREATE_TOPIC);

        NewTopic topic = kafkaConfig.bankCreateTopic();

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.BANK_CREATE_TOPIC, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_DEFAULT_RETENTION_MS),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }

    /**
     * Тестирует конфигурацию топика для обновления банка.
     * Аналогично {@link #testBankCreateTopicConfiguration()}, но для топика обновления.
     */
    @Test
    void testBankUpdateTopicConfiguration() {
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.DEFAULT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_DEFAULT_RETENTION_MS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.BANK_UPDATE_TOPIC_FIELD,
                TestConstants.BANK_UPDATE_TOPIC);

        NewTopic topic = kafkaConfig.bankUpdateTopic();

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.BANK_UPDATE_TOPIC, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_DEFAULT_RETENTION_MS),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }

    /**
     * Тестирует конфигурацию топика для удаления банка.
     * Аналогично {@link #testBankCreateTopicConfiguration()}, но для топика удаления.
     */
    @Test
    void testBankDeleteTopicConfiguration() {
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.DEFAULT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_DEFAULT_RETENTION_MS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.BANK_DELETE_TOPIC_FIELD,
                TestConstants.BANK_DELETE_TOPIC);

        NewTopic topic = kafkaConfig.bankDeleteTopic();

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.BANK_DELETE_TOPIC, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_DEFAULT_RETENTION_MS),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }

    /**
     * Тестирует конфигурацию топика для получения данных о банке.
     * Аналогично {@link #testBankCreateTopicConfiguration()}, но для топика запросов.
     */
    @Test
    void testBankGetTopicConfiguration() {
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.DEFAULT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_DEFAULT_RETENTION_MS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.BANK_GET_TOPIC_FIELD,
                TestConstants.BANK_GET_TOPIC);

        NewTopic topic = kafkaConfig.bankGetTopic();

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.BANK_GET_TOPIC, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_DEFAULT_RETENTION_MS),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }

    /**
     * Тестирует конфигурацию топика для ответов от банка.
     * Отличается от других тестов использованием короткого времени хранения сообщений.
     */
    @Test
    void testBankResponseTopicConfiguration() {
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.SHORT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_SHORT_RETENTION_MS);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.BANK_RESPONSE_TOPIC_FIELD,
                TestConstants.BANK_RESPONSE_TOPIC);

        NewTopic topic = kafkaConfig.bankResponseTopic();

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.BANK_RESPONSE_TOPIC, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_SHORT_RETENTION_MS),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }

    /**
     * Тестирует внутренний метод создания топика с заданным именем.
     * Проверяет корректность передачи альтернативных параметров конфигурации.
     */
    @Test
    void testBuildBankTopicMethod() {
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.PARTITIONS_FIELD, TestConstants.KAFKA_PARTITIONS_ALT);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.REPLICAS_FIELD, TestConstants.KAFKA_REPLICAS_ALT);
        ReflectionTestUtils.setField(kafkaConfig, TestConstants.DEFAULT_RETENTION_MS_FIELD,
                TestConstants.KAFKA_DEFAULT_RETENTION_MS_ALT);

        NewTopic topic = (NewTopic) ReflectionTestUtils.invokeMethod(
                kafkaConfig, TestConstants.BUILD_BANK_TOPIC_METHOD, TestConstants.TEST_TOPIC_NAME);

        assertNotNull(topic, "Топик не должен быть null");
        assertEquals(TestConstants.TEST_TOPIC_NAME, topic.name(),
                "Название топика не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_PARTITIONS_ALT, topic.numPartitions(),
                "Количество партиций не соответствует ожидаемому");
        assertEquals(TestConstants.KAFKA_REPLICAS_ALT, topic.replicationFactor(),
                "Фактор репликации не соответствует ожидаемому");
        assertEquals(String.valueOf(TestConstants.KAFKA_DEFAULT_RETENTION_MS_ALT),
                topic.configs().get(TestConstants.RETENTION_MS_CONFIG),
                "Время хранения сообщений не соответствует ожидаемому");
    }
}
