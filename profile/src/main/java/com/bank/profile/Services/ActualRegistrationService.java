package com.bank.profile.Services;

import com.bank.profile.DTO.ActualRegistrationDto;
import com.bank.profile.Entities.ActualRegistration;

/**
 * Интерфейс для сервиса ActualRegistration
 */
public interface ActualRegistrationService {
    ActualRegistration createActualRegistration(ActualRegistrationDto actualRegistrationDto);
    ActualRegistration updateActualRegistration(ActualRegistrationDto actualRegistrationDto);
    void deleteActualRegistration(Long actualRegistrationId);
    ActualRegistration getActualRegistration(Long actualRegistrationId);
}
