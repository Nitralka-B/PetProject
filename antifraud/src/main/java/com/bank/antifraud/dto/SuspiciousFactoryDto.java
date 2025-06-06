package com.bank.antifraud.dto;

import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.mapper.TransferMapper;
import com.bank.antifraud.util.SuspiciousTransferConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Класс для преобразования в dto по типу получаемой информации
 */

@Component
@RequiredArgsConstructor
public class SuspiciousFactoryDto {

    private final Map<String, TransferMapper> mappers;

    public Object createDto(TransferChecked transfer, String transferType) {
        return getMapper(transferType).eventToDto(transfer);
    }

    public Object updateDto(TransferChecked transfer, String transferType) {
        return getMapper(transferType).updateToDto(transfer);
    }

    public Object createCleanDto(TransferChecked transfer, String transferType) {
        return getMapper(transferType).eventCleanToDto(transfer);
    }

    private TransferMapper getMapper(String transferType) {
        final String beanName = transferType.toLowerCase() + SuspiciousTransferConstants.TRANSFER_MAPPER_IMPL;
        if (mappers.containsKey(beanName)) {
            return mappers.get(beanName);
        } else {
            throw new IllegalArgumentException(SuspiciousTransferConstants.UNKNOWN_TYPE_TRANSFER + transferType +
                    SuspiciousTransferConstants.AVAILABLE_TYPES + mappers.keySet());
        }
    }
}
