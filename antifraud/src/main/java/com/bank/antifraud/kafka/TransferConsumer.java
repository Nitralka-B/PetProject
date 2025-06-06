package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.dto.SuspiciousFactoryDto;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.services.SuspiciousTransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferConsumer {
    private final SuspiciousTransferService<SuspiciousAccountTransferDto> accountService;
    private final SuspiciousTransferService<SuspiciousCardTransferDto> cardService;
    private final SuspiciousTransferService<SuspiciousPhoneTransferDto> phoneService;
    private final SuspiciousFactoryDto factory;
    private final TransferProducer verdictProducer;

    @Value("${transfer.limit.amount:100000}")
    private BigDecimal limit;

    @KafkaListener(topics = KafkaConstants.ACCOUNT_TOPIC, groupId = KafkaConstants.TRANSFER_GROUP,
            errorHandler = "globalKafkaErrorHandler")
    public void listenAccount(@Payload TransferChecked transfer) {
        processTransfer(transfer, KafkaConstants.ACCOUNT, accountService::existsByTransferId);
    }

    @KafkaListener(topics = KafkaConstants.CARD_TOPIC, groupId = KafkaConstants.TRANSFER_GROUP,
            errorHandler = "globalKafkaErrorHandler")
    public void listenCard(@Payload TransferChecked transfer) {
        processTransfer(transfer, KafkaConstants.CARD, cardService::existsByTransferId);
    }

    @KafkaListener(topics = KafkaConstants.PHONE_TOPIC, groupId = KafkaConstants.TRANSFER_GROUP,
            errorHandler = "globalKafkaErrorHandler")
    public void listenPhone(@Payload TransferChecked transfer) {
        processTransfer(transfer, KafkaConstants.PHONE, phoneService::existsByTransferId);
    }

    private void processTransfer(TransferChecked transfer, String transferType,
                                 ExistsChecker existsCheck) {
        final Long transferId = transfer.getId();
        final String purpose = transfer.getPurpose();
        final BigDecimal amount = transfer.getAmount();
        if (amount == null || transferId == null || purpose == null) {
            log.warn(KafkaConstants.NULL_TRANSFER_OBJECT, transferType);
            return;
        }
        final boolean isAmountBlocked = isBlocked(amount);
        final boolean transferExists = existsCheck.exists(transferId);
        if (isAmountBlocked) {
            if (transferExists) {
                update(transferType, transfer, transferId);
            } else {
                create(transferType, transfer);
            }
            verdictProducer.sendVerdict(transferId, transferType, KafkaConstants.BLOCKED, transfer.getPurpose());
        } else if (transferExists) {
            cleanUpdate(transferType, transfer, transferId);
            verdictProducer.sendVerdict(transferId, transferType, KafkaConstants.ALLOWED, transfer.getPurpose());
        } else {
            cleanCreate(transferType, transfer);
            verdictProducer.sendVerdict(transferId, transferType, KafkaConstants.ALLOWED, transfer.getPurpose());
        }
    }

    private void create(String type, TransferChecked t) {
        final Object dto = factory.createDto(t, type);
        applyDtoToService(type, dto, null);
    }

    private void cleanCreate(String type, TransferChecked t) {
        final Object dto = factory.createCleanDto(t, type);
        applyDtoToService(type, dto, null);
    }

    private void update(String type, TransferChecked t, Long transferId) {
        final Optional<Long> entityId = getEntityIdByTransferId(type, transferId);
        final Object dto = factory.createDto(t, type);
        applyDtoToService(type, dto, entityId);
    }

    private void cleanUpdate(String type, TransferChecked t, Long transferId) {
        final Optional<Long> entityId = getEntityIdByTransferId(type, transferId);
        final Object dto = factory.updateDto(t, type);
        applyDtoToService(type, dto, entityId);
    }

    private Optional<Long> getEntityIdByTransferId(String type, Long transferId) {
        switch (type) {
            case KafkaConstants.ACCOUNT:
                return accountService.findIdByTransferId(transferId);
            case KafkaConstants.CARD:
                return cardService.findIdByTransferId(transferId);
            case KafkaConstants.PHONE:
                return phoneService.findIdByTransferId(transferId);
            default:
                log.error("Unknown type: {}", type);
                return null;
        }
    }

    private void applyDtoToService(String type, Object dto, Optional<Long> id) {
        try {
            switch (type) {
                case KafkaConstants.ACCOUNT:
                    if (id == null || id.isEmpty()) {
                        accountService.createTransfer((SuspiciousAccountTransferDto) dto);
                    } else {
                        accountService.updateTransfer(id, (SuspiciousAccountTransferDto) dto);
                    }
                    break;
                case KafkaConstants.CARD:
                    if (id == null || id.isEmpty()) {
                        cardService.createTransfer((SuspiciousCardTransferDto) dto);
                    } else {
                        cardService.updateTransfer(id, (SuspiciousCardTransferDto) dto);
                    }
                    break;
                case KafkaConstants.PHONE:
                    if (id == null || id.isEmpty()) {
                        phoneService.createTransfer((SuspiciousPhoneTransferDto) dto);
                    } else {
                        phoneService.updateTransfer(id, (SuspiciousPhoneTransferDto) dto);
                    }
                    break;
                default:
                    log.error(KafkaConstants.UNKNOWN + type);
                    throw new IllegalArgumentException(KafkaConstants.UNKNOWN + type);
            }
        } catch (Exception e) {
            final String operation = id == null || id.isEmpty() ? KafkaConstants.TYPE_CREATE :
                    KafkaConstants.TYPE_UPDATE;
            log.error(KafkaConstants.TRANSFER_ERROR, operation, type, e.getMessage());
            throw e;
        }
    }

    private boolean isBlocked(BigDecimal amount) {
        if (amount == null) {
            log.warn(KafkaConstants.NULL_AMOUNT);
            throw new IllegalArgumentException(KafkaConstants.NULL_AMOUNT_ERROR);
        }
        return amount.compareTo(limit) > 0;
    }

    @FunctionalInterface
    private interface ExistsChecker {
        boolean exists(Long transferId);
    }
}
