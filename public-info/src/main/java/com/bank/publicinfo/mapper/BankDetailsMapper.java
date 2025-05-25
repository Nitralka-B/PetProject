package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Маппер для преобразования между сущностями {@link BankDetails} и DTO {@link BankDetailsDto}.
 */
@Mapper(componentModel = "spring")
public interface BankDetailsMapper {
    /**
     * Преобразует сущность BankDetails в DTO.
     * @param bankDetails сущность для преобразования
     * @return преобразованный DTO
     */
    BankDetailsDto toDto(BankDetails bankDetails);

    /**
     * Преобразует DTO BankDetailsDto в сущность.
     * @param bankDetailsDto DTO для преобразования
     * @return преобразованная сущность
     */
    BankDetails toEntity(BankDetailsDto bankDetailsDto);

    /**
     * Обновляет существующую сущность BankDetails из DTO.
     * @param dto DTO с обновленными значениями
     * @param entity целевая сущность для обновления
     */
    void updateEntity(BankDetailsDto dto, @MappingTarget BankDetails entity);
}