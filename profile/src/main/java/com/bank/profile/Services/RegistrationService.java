package com.bank.profile.Services;

import com.bank.profile.DTO.RegistrationDto;
import com.bank.profile.Entities.Registration;

/**
 * Интерфейс сервиса Registration
 */
public interface RegistrationService {
    Registration createRegistration(RegistrationDto registrationDto);
    Registration updateRegistration(RegistrationDto registrationDto);
    void deleteRegistration(Long registrationId);
    RegistrationDto getRegistration(Long registrationId);
}
