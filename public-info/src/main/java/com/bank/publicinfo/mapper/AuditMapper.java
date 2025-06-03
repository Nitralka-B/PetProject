package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.Audit;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Маппер для преобразования между сущностями {@link Audit} и DTO {@link AuditDto}.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {
    /**
     * Преобразует сущность Audit в AuditDto.
     * @param audit сущность для преобразования
     * @return преобразованный DTO
     */
    AuditDto toDto(Audit audit);

    /**
     * Преобразует AuditDto в сущность Audit.
     * @param auditDto DTO для преобразования
     * @return преобразованная сущность
     */
    Audit toEntity(AuditDto auditDto);

    /**
     * Преобразует список сущностей Audit в список DTO.
     * @param audits список сущностей для преобразования
     * @return список преобразованных DTO
     */
    List<AuditDto> toDtoList(List<Audit> audits);
}