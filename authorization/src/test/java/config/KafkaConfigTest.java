package config;

import com.bank.authorization.config.KafkaConfig;
import com.bank.authorization.exception.ValidationException;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class KafkaConfigTest {

    @InjectMocks
    private KafkaConfig kafkaConfig;

    private static final String BUILD_TOPIC = "buildTopic";
    private static final String TEST_TOPIC = "testTopic";
    private static final int PARTITION = 3;
    private static final int REPLICATION_FACTOR = 1;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaConfig, "partitions", PARTITION);
        ReflectionTestUtils.setField(kafkaConfig, "replication", (short) REPLICATION_FACTOR);
        ReflectionTestUtils.setField(kafkaConfig, "userCreateTopic", "user-create-topic");
        ReflectionTestUtils.setField(kafkaConfig, "userDeleteTopic", "user-delete-topic");
        ReflectionTestUtils.setField(kafkaConfig, "userUpdateTopic", "user-update-topic");
        ReflectionTestUtils.setField(kafkaConfig, "userGetTopic", "user-get-topic");
    }

    @Test
    @DisplayName("Создание топика с правильными параметрами")
    void buildTopicShouldCreateTopicWithCorrectParams() {

        NewTopic topic = (NewTopic) ReflectionTestUtils.invokeMethod(
                kafkaConfig, BUILD_TOPIC, TEST_TOPIC);

        assertEquals(TEST_TOPIC, topic.name());
        assertEquals(PARTITION, topic.numPartitions());
        assertEquals(REPLICATION_FACTOR, topic.replicationFactor());
    }

    @Test
    @DisplayName("buildTopic должен бросать исключение для пустого имени топика")
    void buildTopicShouldThrowForEmptyTopicName() {

        Exception exception = assertThrows(ValidationException.class, () ->
                ReflectionTestUtils.invokeMethod(kafkaConfig, BUILD_TOPIC, ""));

        assertEquals("Название топика не может быть пустым или null", exception.getMessage());
    }
}
