package config;

import com.bank.authorization.config.KafkaConfig;

import com.bank.authorization.kafka.enumTopic.KafkaTopic;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class KafkaTopicCreationTest {

    private KafkaTemplate<String, Object> kafkaTemplate;

    @Captor
    private ArgumentCaptor<String> logMessageCaptor;

    @Captor
    private ArgumentCaptor<Object> logArgumentCaptor;

    @InjectMocks
    private KafkaConfig kafkaConfig;

    private static final int PARTITIONS = 3;
    private static final int REPLICATION = 1;
    private static final String USER_CREATE_TOPIC = "user-create-topic";
    private static final String USER_DELETE_TOPIC = "user-delete-topic";
    private static final String USER_UPDATE_TOPIC = "user-update-topic";
    private static final String USER_GET_TOPIC = "user-get-topic";

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(kafkaConfig, "partitions", PARTITIONS);
        ReflectionTestUtils.setField(kafkaConfig, "replication", (short) REPLICATION);

        KafkaTopic.USER_CREATE.setTopicName(USER_CREATE_TOPIC);
        KafkaTopic.USER_DELETE.setTopicName(USER_DELETE_TOPIC);
        KafkaTopic.USER_UPDATE.setTopicName(USER_UPDATE_TOPIC);
        KafkaTopic.USER_GET.setTopicName(USER_GET_TOPIC);
    }

    @Test
    @DisplayName("Создание топика для создания пользователей")
    void userCreateTopicShouldBeCreatedCorrectly() {
        NewTopic topic = kafkaConfig.userCreateTopic();
        assertTopicProperties(topic, USER_CREATE_TOPIC);
    }

    @Test
    @DisplayName("Создание топика для удаления пользователей")
    void userDeleteTopicShouldBeCreatedCorrectly() {
        NewTopic topic = kafkaConfig.userDeleteTopic();
        assertTopicProperties(topic, USER_DELETE_TOPIC);
    }

    @Test
    @DisplayName("Создание топика для обновления пользователей")
    void userUpdateTopicShouldBeCreatedCorrectly() {
        NewTopic topic = kafkaConfig.userUpdateTopic();
        assertTopicProperties(topic, USER_UPDATE_TOPIC);
    }

    @Test
    @DisplayName("Создание топика для получения пользователей")
    void userGetTopicShouldBeCreatedCorrectly() {
        NewTopic topic = kafkaConfig.userGetTopic();
        assertTopicProperties(topic, USER_GET_TOPIC);
    }

    private void assertTopicProperties(NewTopic topic, String expectedName) {
        assertThat(topic.name()).isEqualTo(expectedName);
        assertThat(topic.numPartitions()).isEqualTo(PARTITIONS);
        assertThat(topic.replicationFactor()).isEqualTo((short) REPLICATION);
    }
}

