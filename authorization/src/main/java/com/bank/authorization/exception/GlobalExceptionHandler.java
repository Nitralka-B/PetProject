package com.bank.authorization.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component("kafkaGlobalErrorHandler")
public class GlobalExceptionHandler implements KafkaListenerErrorHandler {

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        final Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

        if (cause instanceof KafkaSendException) {
            return handleKafkaSendError((KafkaSendException) cause);
        } else if (cause instanceof EntityNotFoundException) {
            return handleEntityNotFound((EntityNotFoundException) cause);
        } else if (cause instanceof ValidationException) {
            return handleValidationError((ValidationException) cause);
        } else if (cause instanceof AccessDeniedException) {
            return handleAccessDenied((AccessDeniedException) cause);
        } else if (cause instanceof IllegalArgumentException) {
            return handleIllegalArgument((IllegalArgumentException) cause);
        } else if (cause instanceof EntityAlreadyExistsException) {
            return handleEntityExists((EntityAlreadyExistsException) cause);
        } else {
            return handleGenericException(cause);
        }
    }

    private ErrorResponse handleKafkaSendError(KafkaSendException ex) {
        log.error("Ошибка отправления сообщения в Kafka: {}", ex.getMessage());
        return new ErrorResponse(
                "KAFKA_SEND_ERROR",
                "Ошибка отправления сообщения в Kafka",
                Instant.now()
        );
    }

    private ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Сущность не найдена: {}", ex.getMessage());
        return new ErrorResponse(
                "NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
    }

    private ErrorResponse handleValidationError(ValidationException ex) {
        log.warn("Валидация нарушена: {}", ex.getMessage());
        final Map<String, String> details = new HashMap<>();
        details.put("violation", ex.getMessage());

        return new ErrorResponse(
                "VALIDATION_FAILED",
                "Invalid request data",
                Instant.now(),
                details
        );
    }

    private ErrorResponse handleAccessDenied(AccessDeniedException ex) {
        log.warn("Доступ запрещен: {}", ex.getMessage());
        return new ErrorResponse(
                "FORBIDDEN",
                "Доступ запрещен",
                Instant.now()
        );
    }
    private ErrorResponse handleEntityExists(EntityAlreadyExistsException ex) {
        log.error("Entity уже существует: {}", ex.getMessage());
        return new ErrorResponse(
                "ALREADY_EXISTS",
                "Сущность уже существует",
                Instant.now()
        );
    }
    private ErrorResponse handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Неверный аргумент: {}", ex.getMessage());
        return new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage(),
                Instant.now()
        );
    }

    private ErrorResponse handleGenericException(Throwable ex) {
        log.error("Ошибка обработки Kafka: {}", ex.getMessage(), ex);
        return new ErrorResponse(
                "INTERNAL_ERROR",
                "Internal server error",
                Instant.now()
        );
    }

}
