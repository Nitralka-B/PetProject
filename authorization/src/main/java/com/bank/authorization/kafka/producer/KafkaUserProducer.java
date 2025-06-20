package com.bank.authorization.kafka.producer;


import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDeleteRequest;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.exception.KafkaSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Сервис-отправитель пользовательских событий в Kafka.
 * Отвечает за публикацию сообщений о CRUD-операциях с пользователями:
 * - Создание/удаление/обновление пользователей
 * - Запросы информации о пользователях
 * Добавляет JWT-токен в заголовки для авторизации на стороне consumer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaUserProducer {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Value("${spring.kafka.topics.user-create}")
    private String userCreateTopic;

    @Value("${spring.kafka.topics.user-delete}")
    private String userDeleteTopic;

    @Value("${spring.kafka.topics.user-update}")
    private String userUpdateTopic;

    @Value("${spring.kafka.topics.user-get}")
    private String userGetTopic;


    private <T> void sendKafkaMessage(T payload, String topic, String jwtToken) {
        if (payload == null || topic == null || jwtToken == null) {
            throw new IllegalArgumentException("Payload, topic и token не должен быть null");
        }
        try {
            final Message<T> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(AUTHORIZATION, BEARER + jwtToken)
                    .build();
            kafkaTemplate.send(message);
        } catch (Exception e) {
            log.error("Не удалось отправить сообщение в топик {}", topic, e);
            throw new KafkaSendException("Failed to send Kafka message", e);
        }
    }

    public void sendUserCreateEvent(UserCreateRequest request, String jwtToken) {
        log.info("Отправка события создания пользователя: {}", request.getProfileId());
        sendKafkaMessage(request, userCreateTopic, jwtToken);

    }

    public void sendUserDeleteEvent(UserDeleteRequest request, String jwtToken) {
        log.info("Отправка события удаления пользователя: {}", request);
        sendKafkaMessage(request, userDeleteTopic, jwtToken);
    }

    public void sendUserUpdateEvent(UserUpdateRequest request, String jwtToken) {
        log.info("Отправка события обновление пользователя: {}", request);
        sendKafkaMessage(request, userUpdateTopic, jwtToken);
    }

    public void sendUserGetEvent(UserGetRequest request, String jwtToken) {
        log.info("Отправка запроса на получение пользователя: {}", request);
        sendKafkaMessage(request, userGetTopic, jwtToken);
    }

}
