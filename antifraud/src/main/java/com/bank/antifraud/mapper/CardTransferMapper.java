package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entities.SuspiciousCardTransfer;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.util.TransferMapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования в dto или entity,
 * а так же для преобразования поступающих топиков в подозрительные либо чистые запросы
 */


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component("CARD")
public interface CardTransferMapper extends AbstractMapper<SuspiciousCardTransferDto, SuspiciousCardTransfer>,
        TransferMapper {
    SuspiciousCardTransferDto toDto(SuspiciousCardTransfer entity);

    /**
     * Маппинг DTO в сущность.
     * ID игнорируется, так как генерируется на уровне базы данных
     * и не должен переопределяться из DTO
     */
    @Mapping(target = "id", ignore = true)
    SuspiciousCardTransfer toEntity(SuspiciousCardTransferDto dto);

    /**
     * Обновление сущности из DTO.
     * ID игнорируется, так как является идентификатором сущности
     * и не подлежит изменению при обновлении
     */
    @Mapping(target = "id", ignore = true)
    void updateFromDto(SuspiciousCardTransferDto dto, @MappingTarget SuspiciousCardTransfer entity);

    @Override
    default SuspiciousCardTransferDto eventToDto(TransferChecked event) {
        final SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        dto.setCardTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousCardTransferDto updateToDto(TransferChecked event) {
        final SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        dto.setCardTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousCardTransferDto eventCleanToDto(TransferChecked event) {
        final SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        dto.setCardTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }
}
