package com.bank.profile.Mappers;

import com.bank.profile.DTO.RegistrationDto;
import com.bank.profile.Entities.Registration;
import org.mapstruct.Mapper;

/**
 * Mapper для регистрации
 */
@Mapper(componentModel = "spring")
public interface RegistrationMapper {

    RegistrationDto registrationtoRegistrationDto(Registration registration);
    Registration registrationDtoToRegistration(RegistrationDto registrationDto);
}
