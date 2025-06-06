package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuspiciousTransferProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private void sendToKafka(String topic, String message) {
        if (message == null) {
            log.warn(KafkaConstants.EMPTY_MESSAGE, topic);
            return;
        }
        try {
            kafkaTemplate.send(topic, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug(KafkaConstants.SUCCESSFUL, topic);
                        } else {
                            log.error(KafkaConstants.ERROR_SENDING, topic, ex);
                        }
                    });
        } catch (Exception e) {
            log.error(KafkaConstants.CRITICAL_ERROR, topic, e);
        }
    }

    public void sendCreate(String payload) {
        sendToKafka(KafkaConstants.SUSPICIOUS_CREATE, payload);
    }

    public void sendUpdate(String payload) {
        sendToKafka(KafkaConstants.SUSPICIOUS_UPDATE, payload);
    }

    public void sendDelete(String payload) {
        sendToKafka(KafkaConstants.SUSPICIOUS_DELETE, payload);
    }

    public void sendGet(String payload) {
        sendToKafka(KafkaConstants.SUSPICIOUS_GET, payload);
    }
}
