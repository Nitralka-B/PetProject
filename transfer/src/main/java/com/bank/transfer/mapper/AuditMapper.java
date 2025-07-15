package com.bank.transfer.mapper;

import com.bank.transfer.DTO.AuditDTO;
import com.bank.transfer.entities.Audit;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDTO toDto(Audit entity);
    Audit toEntity(AuditDTO dto);
}
