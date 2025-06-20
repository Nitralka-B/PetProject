package com.bank.profile.Exceptions;

import com.bank.profile.Utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import java.time.Instant;


/**
 * Глобальный обработчик исключений выкидывающихся в Kafka
 */
@Slf4j
@RequiredArgsConstructor
@Component("profileKafkaErrorHandler")
public class GlobalExceptionHandler implements KafkaListenerErrorHandler {

    private final KafkaTemplate<String, ErrorResponse> kafkaTemplate;
    @Value("${spring.kafka.topics.profile-errors}")
    private String errorsTopic;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        final Throwable cause = exception.getCause() != null ? exception.getCause() : exception;

        if (cause instanceof EntityNotFoundException) {
            return handleEntityNotFound((EntityNotFoundException) cause);
        } else if (cause instanceof EntityDublicateException) {
            return handleEntityDuplicate((EntityDublicateException) cause);
        } else if (cause instanceof NotAuthorizedException) {
            return handleNoAuthorized((NotAuthorizedException) cause);
        } else {
            return handleGenericError(cause);
        }
    }

    private ErrorResponse handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        final ErrorResponse e = new ErrorResponse(
                ErrorCode.ENTITY_NOT_FOUND.name(),
                ex.getMessage(),
                Instant.now()
        );
        kafkaTemplate.send(errorsTopic, e);
        return e;
    }

    private ErrorResponse handleEntityDuplicate(EntityDublicateException ex) {
        log.warn("Duplicate entity: {}", ex.getMessage());
        final ErrorResponse e = new ErrorResponse(
                ErrorCode.ENTITY_DUPLICATE.name(),
                ex.getMessage(),
                Instant.now()
        );
        kafkaTemplate.send(errorsTopic, e);
        return e;
    }

    private ErrorResponse handleNoAuthorized(NotAuthorizedException ex) {
        log.warn("Not authorized: {}", ex.getMessage());
        final ErrorResponse e = new ErrorResponse(
                ErrorCode.NO_AUTHORIZATION.name(),
                ex.getMessage(),
                Instant.now()
        );
        kafkaTemplate.send(errorsTopic, e);
        return e;
    }

    private ErrorResponse handleGenericError(Throwable ex) {
        log.error("Kafka processing error: {}", ex.getMessage(), ex);
        final ErrorResponse e = new ErrorResponse(
                "INTERNAL_ERROR",
                "Internal server error",
                Instant.now()
        );
        kafkaTemplate.send(errorsTopic, e);
        return e;
    }
}
