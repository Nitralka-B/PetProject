package com.bank.transfer.mapper;

import com.bank.transfer.DTO.AccountTransferDto;
import com.bank.transfer.entities.AccountTransfer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AccountTransferMapper {
    AccountTransfer toEntity(AccountTransferDto accountTransferDTO);
    AccountTransferDto toDTO(AccountTransfer accountTransfer);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(AccountTransferDto dto, @MappingTarget AccountTransfer entity);

}
