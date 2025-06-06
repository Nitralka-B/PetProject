package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.SuspiciousPhoneTransfer;
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
@Component("PHONE")
public interface PhoneTransferMapper extends AbstractMapper<SuspiciousPhoneTransferDto, SuspiciousPhoneTransfer>,
        TransferMapper {
    SuspiciousPhoneTransferDto toDto(SuspiciousPhoneTransfer entity);

    /**
     * Маппинг DTO в сущность.
     * ID игнорируется, так как генерируется на уровне базы данных
     * и не должен переопределяться из DTO
     */
    @Mapping(target = "id", ignore = true)
    SuspiciousPhoneTransfer toEntity(SuspiciousPhoneTransferDto dto);

    /**
     * Обновление сущности из DTO.
     * ID игнорируется, так как является идентификатором сущности
     * и не подлежит изменению при обновлении
     */
    @Mapping(target = "id", ignore = true)
    void updateFromDto(SuspiciousPhoneTransferDto dto, @MappingTarget SuspiciousPhoneTransfer entity);

    @Override
    default SuspiciousPhoneTransferDto eventToDto(TransferChecked event) {
        final SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setPhoneTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousPhoneTransferDto updateToDto(TransferChecked event) {
        final SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setPhoneTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousPhoneTransferDto eventCleanToDto(TransferChecked event) {
        final SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setPhoneTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }
}
