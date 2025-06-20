package com.bank.profile.Mappers;

import com.bank.profile.DTO.PassportDto;
import com.bank.profile.Entities.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для паспорта
 */
@Mapper(componentModel = "spring", uses = {RegistrationMapper.class})
public interface PassportMapper {

    @Mapping(source = "registration", target = "registrationDto")
    PassportDto passportToPassportDto(Passport passport);

    @Mapping(source = "registrationDto", target = "registration")
    Passport passportDtoToPassport(PassportDto passportDto);
}
