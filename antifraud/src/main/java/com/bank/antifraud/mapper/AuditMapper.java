package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entities.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

/**
 * Преобразует сущности аудита в DTO и обратно
 */
@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface AuditMapper {
    AuditDto toDtoAudit(Audit audit);
    
    /**
     * Преобразует AuditDto в Entity.
     * При преобразовании id устанавливается в null,
     * чтобы гарантировать создание новой записи
     */
    @Mapping(target = "id", ignore = true)
    Audit toEntityAudit(AuditDto auditDto);
}
