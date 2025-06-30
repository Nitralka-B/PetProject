package kafka.producer;

import com.bank.authorization.exception.KafkaSendException;
import com.bank.authorization.kafka.producer.KafkaUserProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.util.ReflectionTestUtils;



import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
public class KafkaUserProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaUserProducer kafkaUserProducer;

    @Captor
    private ArgumentCaptor<Message<Object>> messageCaptor;

    private static final String TEST_JWT_TOKEN = "test.jwt.token";
    private static final Object TEST_PAYLOAD = new Object();
    private static final String USER_CREATE_TOPIC = "user-create-topic";
    private static final String SEND_KAFKA_MESSAGE = "sendKafkaMessage";


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaUserProducer, "userCreateTopic", USER_CREATE_TOPIC);
    }

    @Test
    @DisplayName("Успешная отправка сообщения")
    void sendKafkaMessageSuccess() {

        ReflectionTestUtils.invokeMethod(
                kafkaUserProducer,
                SEND_KAFKA_MESSAGE,
                TEST_PAYLOAD, USER_CREATE_TOPIC, TEST_JWT_TOKEN
        );

        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<Object> sentMessage = messageCaptor.getValue();

        assertAll(
                () -> assertEquals(TEST_PAYLOAD, sentMessage.getPayload()),
                () -> assertEquals(USER_CREATE_TOPIC, sentMessage.getHeaders().get(KafkaHeaders.TOPIC)),
                () -> assertEquals("Bearer test.jwt.token", sentMessage.getHeaders().get("Authorization"))
        );
    }

    @Test
    @DisplayName("Ошибка при null payload")
    void sendKafkaMessageNullPayloadThrowsException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ReflectionTestUtils.invokeMethod(
                        kafkaUserProducer,
                        SEND_KAFKA_MESSAGE,
                        null, USER_CREATE_TOPIC, TEST_JWT_TOKEN
                )
        );

        assertEquals("Payload, topic и token не должен быть null", exception.getMessage());
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    @DisplayName("Ошибка при отправке в Kafka")
    void sendKafkaMessageKafkaErrorThrowsKafkaSendException() {
        RuntimeException kafkaException = new RuntimeException("Kafka error");
        doThrow(kafkaException).when(kafkaTemplate).send(any(Message.class));

        KafkaSendException exception = assertThrows(
                KafkaSendException.class,
                () -> ReflectionTestUtils.invokeMethod(
                        kafkaUserProducer,
                        SEND_KAFKA_MESSAGE,
                        TEST_PAYLOAD, USER_CREATE_TOPIC, TEST_JWT_TOKEN
                )
        );

        assertEquals("Failed to send Kafka message", exception.getMessage());
        assertSame(kafkaException, exception.getCause());
    }
}
