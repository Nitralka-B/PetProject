package com.bank.publicinfo.kafka.consumer;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Потребитель Kafka для обработки операций с банковскими реквизитами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BankKafkaConsumer {
    private static final String CREATE_TOPIC = "public-info.bank.create";
    private static final String UPDATE_TOPIC = "public-info.bank.update";
    private static final String DELETE_TOPIC = "public-info.bank.delete";
    private static final String GROUP_ID = "public-info-group";

    private final BankDetailsService bankDetailsService;

    /**
     * Обрабатывает создание банковских реквизитов.
     * @param dto DTO с данными банковских реквизитов
     * @param key ключ сообщения
     * @param acknowledgment подтверждение обработки
     */
    @KafkaListener(topics = CREATE_TOPIC, groupId = GROUP_ID)
    public void consumeCreate(
            @Payload BankDetailsDto dto,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment acknowledgment) {
        Assert.notNull(dto, "BankDetailsDto cannot be null");
        Assert.notNull(key, "Message key cannot be null");

        try {
            log.info("Processing bank creation with key {}: {}", key, dto);

            // Проверяем существование банка перед созданием
            if (bankDetailsService.existsByBik(dto.getBik())) {
                log.warn("Bank with BIK {} already exists, skipping creation", dto.getBik());
            } else {
                bankDetailsService.create(dto);
                log.info("Successfully processed bank creation with key {}", key);
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing bank creation with key {}", key, e);
            acknowledgment.acknowledge();
        }
    }

    /**
     * Обрабатывает обновление банковских реквизитов.
     * @param dto DTO с обновленными данными
     * @param key ключ сообщения (ID банка)
     * @param acknowledgment подтверждение обработки
     */
    @KafkaListener(topics = UPDATE_TOPIC, groupId = GROUP_ID)
    public void consumeUpdate(
            @Payload BankDetailsDto dto,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            Acknowledgment acknowledgment) {
        Assert.notNull(dto, "BankDetailsDto cannot be null");
        Assert.notNull(key, "Message key cannot be null");

        try {
            log.info("Processing update for bank ID {}: {}", key, dto);
            long bankId = Long.parseLong(key);

            if (!bankDetailsService.existsById(bankId)) {
                log.warn("Bank with ID {} not found, skipping update", bankId);
            } else {
                bankDetailsService.update(bankId, dto);
                log.info("Successfully processed update for bank ID {}", bankId);
            }

            acknowledgment.acknowledge();
        } catch (NumberFormatException e) {
            log.error("Invalid bank ID format: {}", key, e);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing bank update with key {}", key, e);
            acknowledgment.acknowledge();
        }
    }

    /**
     * Обрабатывает удаление банковских реквизитов.
     * @param bankIdStr строковое представление ID банка
     * @param acknowledgment подтверждение обработки
     */
    @KafkaListener(
            topics = DELETE_TOPIC,
            groupId = GROUP_ID,
            containerFactory = "bankDeleteKafkaListenerContainerFactory"
    )
    public void handleDelete(
            @Payload String bankIdStr,
            Acknowledgment acknowledgment) {
        try {
            log.info("Processing delete for bank ID: {}", bankIdStr);
            Long bankId = Long.parseLong(bankIdStr);

            if (!bankDetailsService.existsById(bankId)) {
                log.warn("Bank with ID {} not found, skipping deletion", bankId);
            } else {
                bankDetailsService.delete(bankId);
                log.info("Successfully deleted bank with ID: {}", bankId);
            }

            acknowledgment.acknowledge();
        } catch (NumberFormatException e) {
            log.error("Invalid bank ID format: {}", bankIdStr, e);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error processing bank deletion for ID {}", bankIdStr, e);
            acknowledgment.acknowledge();
            throw e;
        }
    }
}