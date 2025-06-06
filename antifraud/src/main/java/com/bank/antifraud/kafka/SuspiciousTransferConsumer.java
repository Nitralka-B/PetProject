package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Класс для получения подозрительных
 * данных из SuspiciousTransferProducer.
 */

@Component
@Slf4j
public class SuspiciousTransferConsumer {

    @KafkaListener(
            topics = {KafkaConstants.SUSPICIOUS_CREATE,
                    KafkaConstants.SUSPICIOUS_UPDATE,
                    KafkaConstants.SUSPICIOUS_DELETE,
                    KafkaConstants.SUSPICIOUS_GET},
            groupId = KafkaConstants.AUDIT_GROUP)
    public void onMessage(ConsumerRecord<String, String> record) {
        if (record.topic() == null) {
            log.warn(KafkaConstants.EMPTY_MESSAGE, record.topic());
            return;
        }
        try {
            switch (record.topic()) {
                case KafkaConstants.SUSPICIOUS_CREATE -> handleCreate(record.value());
                case KafkaConstants.SUSPICIOUS_UPDATE -> handleUpdate(record.value());
                case KafkaConstants.SUSPICIOUS_DELETE -> handleDelete(record.value());
                case KafkaConstants.SUSPICIOUS_GET -> handleGet(record.value());
                default -> log.warn(KafkaConstants.UNKNOWN_TOPIC, record.topic());
            }

        } catch (KafkaException e) {
            log.error(KafkaConstants.TOPIC_ERROR, e.getMessage());
            throw new KafkaException(KafkaConstants.TOPIC_ERROR_MSG);
        }
    }

    private void handleGet(String value) {
        log.info(KafkaConstants.TOPIC_RECEIVED, "GET", value);
    }

    private void handleDelete(String value) {
        log.info(KafkaConstants.TOPIC_RECEIVED, "DELETE", value);
    }

    private void handleUpdate(String value) {
        log.info(KafkaConstants.TOPIC_RECEIVED, "UPDATE", value);
    }

    private void handleCreate(String value) {
        log.info(KafkaConstants.TOPIC_RECEIVED, "CREATE", value);
    }
}
