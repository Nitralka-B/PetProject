package com.bank.profile.Mappers;

import com.bank.profile.DTO.AuditDto;
import com.bank.profile.Entities.Audit;
import org.mapstruct.Mapper;

/**
 * Mapper для аудита
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDto toAuditDto(Audit audit);
    Audit toAudit(AuditDto dto);
}
