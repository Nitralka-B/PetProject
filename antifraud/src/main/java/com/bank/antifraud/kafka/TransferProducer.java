package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import com.bank.antifraud.entities.TransferVerdict;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Класс для отправки данных в другие микросервисы.
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class TransferProducer {
    private final KafkaTemplate<String, TransferVerdict> kafkaTemplate;

    public void sendVerdict(Long transferId, String transferType, String verdict, String reason) {
        if (transferId == null || transferType == null || verdict == null || reason == null) {
            log.error(KafkaConstants.EMPTY_PARAMS_TRANSFER, transferId, transferType, verdict, reason);
            throw new KafkaException(KafkaConstants.EMPTY_PARAMS_TRANSFER);
        }
        try {
            final TransferVerdict payload = new TransferVerdict(transferId, transferType, verdict, reason);
            kafkaTemplate.send(KafkaConstants.TRANSFER_VERDICT, payload)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug(KafkaConstants.SUCCESSFUL, KafkaConstants.TRANSFER_VERDICT);
                        } else {
                            log.error(KafkaConstants.ERROR_SENDING, KafkaConstants.TRANSFER_VERDICT, ex);
                        }
                    });
            log.info(KafkaConstants.SEND_RESULT, payload);
        } catch (KafkaException e) {
            log.error(KafkaConstants.CRITICAL_ERROR, KafkaConstants.TRANSFER_VERDICT, e);
        }
    }
}
