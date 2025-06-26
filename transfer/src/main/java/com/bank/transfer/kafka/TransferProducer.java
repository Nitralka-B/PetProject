package com.bank.transfer.kafka;

import com.bank.transfer.DTO.AccountTransferDto;
import com.bank.transfer.DTO.CardTransferDto;
import com.bank.transfer.DTO.PhoneTransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTransferToKafka(Object dto) {
        final String topic;

        if (dto instanceof AccountTransferDto) {
            topic = "transfer.account";
        } else if (dto instanceof CardTransferDto) {
            topic = "transfer.card";
        } else if (dto instanceof PhoneTransferDto) {

            topic = "transfer.phone";
        } else {
            log.error("Неизвестный тип DTO: {}", dto.getClass().getSimpleName());
            throw new IllegalArgumentException("Unsupported transfer DTO type");
        }

        kafkaTemplate.send(topic, dto);
        log.info("Сообщение отправлено в Kafka: topic={}, payload={}", topic, dto);

    }

}
