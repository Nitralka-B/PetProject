package kafka.producer;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDeleteRequest;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserUpdateRequest;
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

import static com.bank.authorization.entity.Role.ADMIN;
import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaUserProducerEventsTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaUserProducer kafkaUserProducer;

    @Captor
    private ArgumentCaptor<Message<Object>> messageCaptor;

    private static final String TEST_JWT_TOKEN = "test.jwt.token";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Long USER_PROFILE_ID = 111L;
    private static final String PASSWORD = "encodedPassword111";
    private static final String NEW_PASSWORD = "newEncodedPassword111";
    private static final String HEADER_KEY = "Authorization";
    private static final String USER_CREATE_TOPIC = "user-create-topic";
    private static final String USER_DELETE_TOPIC = "user-delete-topic";
    private static final String USER_UPDATE_TOPIC = "user-update-topic";
    private static final String USER_GET_TOPIC = "user-get-topic";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaUserProducer, "userCreateTopic", USER_CREATE_TOPIC);
        ReflectionTestUtils.setField(kafkaUserProducer, "userDeleteTopic", USER_DELETE_TOPIC);
        ReflectionTestUtils.setField(kafkaUserProducer, "userUpdateTopic", USER_UPDATE_TOPIC);
        ReflectionTestUtils.setField(kafkaUserProducer, "userGetTopic", USER_GET_TOPIC);
    }

    @Test
    @DisplayName("Успешная отправка события создания пользователя")
    void sendUserCreateEventSuccess() {

        UserCreateRequest request = new UserCreateRequest(USER_PROFILE_ID, PASSWORD, USER);

        kafkaUserProducer.sendUserCreateEvent(request, TEST_JWT_TOKEN);

        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<Object> message = messageCaptor.getValue();

        assertAll(
                () -> assertEquals(request, message.getPayload()),
                () -> assertEquals(USER_CREATE_TOPIC, message.getHeaders().get(KafkaHeaders.TOPIC)),
                () -> assertEquals(BEARER_PREFIX + TEST_JWT_TOKEN, message.getHeaders().get(HEADER_KEY))
        );

    }

    @Test
    @DisplayName("Успешная отправка события удаления пользователя")
    void sendUserDeleteEventSuccess() {

        UserDeleteRequest request = new UserDeleteRequest(USER_PROFILE_ID);

        kafkaUserProducer.sendUserDeleteEvent(request, TEST_JWT_TOKEN);

        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<Object> message = messageCaptor.getValue();

        assertAll(
                () -> assertEquals(request, message.getPayload()),
                () -> assertEquals(USER_DELETE_TOPIC, message.getHeaders().get(KafkaHeaders.TOPIC)),
                () -> assertEquals(BEARER_PREFIX + TEST_JWT_TOKEN, message.getHeaders().get(HEADER_KEY))
        );
    }

    @Test
    @DisplayName("Успешная отправка события обновления пользователя")
    void sendUserUpdateEventSuccess() {

        UserUpdateRequest request = new UserUpdateRequest(USER_PROFILE_ID, NEW_PASSWORD, ADMIN);

        kafkaUserProducer.sendUserUpdateEvent(request, TEST_JWT_TOKEN);

        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<Object> message = messageCaptor.getValue();

        assertAll(
                () -> assertEquals(request, message.getPayload()),
                () -> assertEquals(USER_UPDATE_TOPIC, message.getHeaders().get(KafkaHeaders.TOPIC)),
                () -> assertEquals(BEARER_PREFIX + TEST_JWT_TOKEN, message.getHeaders().get(HEADER_KEY))
        );
    }

    @Test
    @DisplayName("Успешная отправка запроса получения пользователя")
    void sendUserGetEventSuccess() {

        UserGetRequest request = new UserGetRequest(USER_PROFILE_ID, ADMIN);

        kafkaUserProducer.sendUserGetEvent(request, TEST_JWT_TOKEN);

        verify(kafkaTemplate).send(messageCaptor.capture());
        Message<Object> message = messageCaptor.getValue();

        assertAll(
                () -> assertEquals(request, message.getPayload()),
                () -> assertEquals(USER_GET_TOPIC, message.getHeaders().get(KafkaHeaders.TOPIC)),
                () -> assertEquals(BEARER_PREFIX + TEST_JWT_TOKEN, message.getHeaders().get(HEADER_KEY))
        );
    }

}
