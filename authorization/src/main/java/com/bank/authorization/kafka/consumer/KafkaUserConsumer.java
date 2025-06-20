package com.bank.authorization.kafka.consumer;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDeleteRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserGetResponse;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * Основной Kafka Consumer для обработки пользовательских событий.
 * Обрабатывает CRUD-операции с пользователями через Kafka-сообщения:
 * - Создание/удаление/обновление пользователей
 * - Получение информации о пользователях
 * - JWT-аутентификация и авторизация для защищенных операций
 * Интегрируется с UserService и KafkaJwtValidator для безопасной обработки.
 */
@Service
@Slf4j
@Validated
@RequiredArgsConstructor
public class KafkaUserConsumer {

    private final UserService userService;
    private final KafkaMessageHandler handler;


    @KafkaListener(topics = "${spring.kafka.topics.user-create}",
            groupId = "${spring.kafka.consumer.group-id}",
            errorHandler = "kafkaGlobalErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.authorization.dto.UserCreateRequest"
            }
    )
    public void handleUserCreate(@Valid @Payload UserCreateRequest request) {

        handler.processAdminAction(request, null, req -> {
            UserDto userDto = userService.createUser(req);
            log.info("User created. ID: {}, profileId: {}",
                    userDto.getId(), userDto.getProfileId());
        });
    }


    @KafkaListener(topics = "${spring.kafka.topics.user-delete}",
            groupId = "${spring.kafka.consumer.group-id}",
            errorHandler = "kafkaGlobalErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.authorization.dto.UserDeleteRequest"
            }
    )
    public void handleUserDelete(@Valid @Payload UserDeleteRequest request,
                                 @Header("Authorization") String token) {

        handler.processAdminAction(request, token, req -> {
            userService.deleteUser(req.getProfileId());
            log.info("User deleted. profileId: {}", req.getProfileId());
        });
    }


    @KafkaListener(topics = "${spring.kafka.topics.user-update}",
            groupId = "${spring.kafka.consumer.group-id}",
            errorHandler = "kafkaGlobalErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.authorization.dto.UserUpdateRequest"
            })
    public void handleUserUpdate(@Valid @Payload UserUpdateRequest request,
                                 @Header("Authorization") String token) {

        handler.processAdminAction(request, token, req -> {
            userService.updateUserRole(req);
            log.info("User updated. profileId: {}", req.getProfileId());
        });
    }


    @KafkaListener(topics = "${spring.kafka.topics.user-get}",
            groupId = "${spring.kafka.consumer.group-id}",
            errorHandler = "kafkaGlobalErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.authorization.dto.UserGetRequest"
            })
    public UserGetResponse handleUserGetRequest(@Valid @Payload UserGetRequest request,
                                                @Header("Authorization") String token) {
        return handler.processAdminActionWithResult(request, token, req -> {
            UserGetResponse response = userService.getAllUsers(req);
            KafkaMessageHandler.logResponse("users", response.getUsers());
            return response;
        });
    }
}
