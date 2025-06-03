package com.bank.publicinfo.kafka.producer;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BankResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Продюсер Kafka для отправки сообщений о банковских реквизитах.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BankKafkaProducer {
    private static final String RESPONSE_TOPIC = "public-info.bank.response";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Отправляет ответ с банковскими реквизитами.
     * @param response DTO ответа
     */
    public void sendBankResponse(BankResponseDto response) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                RESPONSE_TOPIC,
                response.getRequestId().toString(),
                response
        );
        // Добавляем маркер типа сообщения
        record.headers().add("message-type", "response".getBytes());

        sendRecordWithCallback(record, response.getRequestId().toString());
    }

    /**
     * Отправляет запись в Kafka с обработкой результата.
     * @param record запись для отправки
     * @param key ключ записи
     */
    private void sendRecordWithCallback(ProducerRecord<String, Object> record, String key) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Successfully sent to {}: key {}, partition {}",
                        record.topic(),
                        key,
                        result.getRecordMetadata().partition());
            } else {
                log.error("Failed to send to {}: key {}", record.topic(), key, ex);
            }
        });
    }
}