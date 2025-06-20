package com.bank.authorization.mapper;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.Audit;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования AuditDto в сущность Audit.
 * Использует MapStruct для автоматической генерации реализации.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    Audit toEntity(AuditDto auditDto);

    AuditDto toDto(Audit audit);


}
