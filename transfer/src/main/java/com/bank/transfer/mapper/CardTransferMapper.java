package com.bank.transfer.mapper;


import com.bank.transfer.DTO.CardTransferDto;
import com.bank.transfer.entities.CardTransfer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardTransferMapper {

    CardTransfer toEntity(CardTransferDto cardTransferDTO);
    CardTransferDto toDTO(CardTransfer cardTransfer);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CardTransferDto dto, @MappingTarget CardTransfer entity);
}
