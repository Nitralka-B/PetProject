package com.bank.transfer.kafka;

import com.bank.transfer.DTO.AccountDetailsDto;
import com.bank.transfer.services.AccountDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountDetailsConsumer {
    private final AccountDetailsService accountDetailsService;


    @KafkaListener(topics = "account.get",
            groupId = "transfer-group",
            containerFactory = "accountDetailsKafkaListenerContainerFactory")
    public void consumeAccountDetails(@Payload AccountDetailsDto accountDetailsDto) {
        log.info("Получены данные счёта: {}", accountDetailsDto);

        accountDetailsService.processAccountDetails(accountDetailsDto);
    }


}
