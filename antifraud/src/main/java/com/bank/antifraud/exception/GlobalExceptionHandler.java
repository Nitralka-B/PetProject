package com.bank.antifraud.exception;

import com.bank.antifraud.dto.ErrorResponseDto;
import com.bank.antifraud.util.ExceptionConstans;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений Kafka-слушателей.
 * Перехватывает ошибки из методов, аннотированных {@code @KafkaListener},
 * логирует и формирует подробный {@link ErrorResponseDto} для последующего анализа (метрика/лог).
 */
@Component("globalKafkaErrorHandler")
@Slf4j
public class GlobalExceptionHandler implements KafkaListenerErrorHandler {
    private static final int NUMBER = 500;
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException ex) {
        final String topic = String.valueOf(message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC));
        final Object offsetHeader = message.getHeaders().get(KafkaHeaders.OFFSET);
        final long offset = offsetHeader instanceof Long ? (Long) offsetHeader : -1L;
        Object key = null;
        if (message.getPayload() instanceof ConsumerRecord<?, ?> record) {
            key = record.key();
        }
        final ErrorResponseDto response = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(NUMBER)
                .error("KafkaListenerException")
                .message(ex.getMessage())
                .path(topic + ":" + offset)
                .build();

        log.error(ExceptionConstans.LOG_ERROR_FORMAT + ", key={}",
                response.getStatus(), response.getMessage(), response.getPath(), key, ex);
        return null;
    }
}
