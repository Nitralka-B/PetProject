package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entities.SuspiciousAccountTransfer;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.util.TransferMapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

/**
 * MapStruct mapper for account transfers. Also acts as TransferMapper for the factory.
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component("ACCOUNT")
public interface AccountTransferMapper extends AbstractMapper<SuspiciousAccountTransferDto, SuspiciousAccountTransfer>,
        TransferMapper {
    @Override
    SuspiciousAccountTransferDto toDto(SuspiciousAccountTransfer entity);

    /**
     * Маппинг DTO в сущность.
     * ID игнорируется, так как генерируется на уровне базы данных
     * и не должен переопределяться из DTO
     */
    @Override
    @Mapping(target = "id", ignore = true)
    SuspiciousAccountTransfer toEntity(SuspiciousAccountTransferDto dto);

    /**
     * Обновление сущности из DTO.
     * ID игнорируется, так как является идентификатором сущности
     * и не подлежит изменению при обновлении
     */
    @Override
    @Mapping(target = "id", ignore = true)
    void updateFromDto(SuspiciousAccountTransferDto dto, @MappingTarget SuspiciousAccountTransfer entity);

    // === TransferMapper implementation ===
    @Override
    default SuspiciousAccountTransferDto eventToDto(TransferChecked event) {
        final SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setAccountTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousAccountTransferDto updateToDto(TransferChecked event) {
        final SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setAccountTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }

    @Override
    default SuspiciousAccountTransferDto eventCleanToDto(TransferChecked event) {
        final SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setAccountTransferId(event.getId());
        dto.setIsBlocked(TransferMapperConstants.NOT_BLOCKED);
        dto.setIsSuspicious(TransferMapperConstants.NOT_SUSPICIOUS);
        dto.setBlockedReason(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        dto.setSuspiciousReason(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
        return dto;
    }
}
