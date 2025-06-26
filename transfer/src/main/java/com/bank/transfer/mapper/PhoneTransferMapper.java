package com.bank.transfer.mapper;


import com.bank.transfer.DTO.PhoneTransferDto;
import com.bank.transfer.entities.PhoneTransfer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PhoneTransferMapper {
    PhoneTransfer toEntity(PhoneTransferDto phoneTransferDTO);
    PhoneTransferDto toDTO(PhoneTransfer phoneTransfer);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PhoneTransferDto dto, @MappingTarget PhoneTransfer entity);
}
