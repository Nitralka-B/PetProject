package com.bank.publicinfo.kafka.consumer;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BankResponseDto;
import com.bank.publicinfo.kafka.producer.BankKafkaProducer;
import com.bank.publicinfo.service.BankDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Потребитель Kafka для обработки запросов на получение банковских реквизитов.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankGetConsumer {
    private final BankDetailsService bankDetailsService;
    private final BankKafkaProducer producer;
    private final ObjectMapper objectMapper;

    /**
     * Обрабатывает запрос на получение банковских реквизитов.
     *
     * @param bankIdStr строковое представление ID банка
     */
    @KafkaListener(
            topics = "public-info.bank.get",
            containerFactory = "bankGetKafkaListenerContainerFactory"
    )
    public void handleGetRequest(@Payload String bankIdStr) {
        UUID requestId = UUID.randomUUID();
        try {
            log.debug("Processing GET request for bank ID: {}", bankIdStr);

            Long bankId = parseBankId(bankIdStr);

            BankDetailsDto dto = bankDetailsService.getById(bankId);

            producer.sendBankResponse(new BankResponseDto(
                    requestId,
                    dto,
                    LocalDateTime.now(),
                    "SUCCESS"
            ));

            log.info("Successfully processed GET request for bank ID: {}", bankId);
        } catch (NumberFormatException e) {
            log.error("Invalid bank ID format: {}", bankIdStr);
            producer.sendBankResponse(new BankResponseDto(
                    requestId,
                    null,
                    LocalDateTime.now(),
                    "ERROR: Invalid bank ID format"
            ));
        } catch (Exception e) {
            log.error("Error processing request for bank ID: {}", bankIdStr, e);
            producer.sendBankResponse(new BankResponseDto(
                    requestId,
                    null,
                    LocalDateTime.now(),
                    "ERROR: " + e.getMessage()
            ));
        }
    }

    /**
     * Парсит строковый ID банка в Long.
     *
     * @param bankIdStr строковое представление ID банка
     * @return числовой ID банка
     * @throws NumberFormatException если строка не содержит цифр
     */
    private Long parseBankId(String bankIdStr) {
        String cleanId = bankIdStr.replaceAll("[^\\d]", "");
        if (cleanId.isEmpty()) {
            throw new NumberFormatException("Bank ID contains no digits");
        }
        return Long.parseLong(cleanId);
    }
}