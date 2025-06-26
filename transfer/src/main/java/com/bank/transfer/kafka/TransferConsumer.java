package com.bank.transfer.kafka;

import com.bank.transfer.DTO.IncomingTransferDto;
import com.bank.transfer.services.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransferConsumer {

    private final TransferService transferService;


    @KafkaListener(topics = "transfer.incoming",
            groupId = "transfer-group",
            containerFactory = "incomingTransferKafkaListenerContainerFactory")
    public void consumeTransfer(@Payload IncomingTransferDto dto) {
        log.info("Получены данные из топика: {}", dto);
        try {

            transferService.processTransfer(dto);
        } catch (Exception e) {
            log.error("Failed to process transfer: {}", dto, e);
        }
    }


}
