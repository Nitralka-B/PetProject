package com.bank.profile.Services;

import com.bank.profile.DTO.RegistrationDto;
import com.bank.profile.Entities.Registration;
import com.bank.profile.Mappers.RegistrationMapper;
import com.bank.profile.Repositories.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса RegistrationService
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;

    /**
     * Метод для создания записей
     */
    @Override
    public Registration createRegistration(RegistrationDto registrationDto) {
        final Registration registration = registrationMapper.registrationDtoToRegistration(registrationDto);
        return registrationRepository.save(registration);
    }

    /**
     * Метод для обновления существующих записей
     */
    @Override
    public Registration updateRegistration(RegistrationDto registrationDto) {
        final Registration registration = registrationMapper.registrationDtoToRegistration(registrationDto);
        return registrationRepository.save(registration);
    }

    /**
     * Метод для удаления записей
     */
    @Override
    public void deleteRegistration(Long RegistrationId) {
        final Registration registration = registrationRepository.getById(RegistrationId);
        registrationRepository.delete(registration);
    }

    /**
     * Метод для получения записей
     */
    @Override
    public RegistrationDto getRegistration(Long RegistrationId) {
        final Registration registration = registrationRepository.getById(RegistrationId);
        return registrationMapper.registrationtoRegistrationDto(registration);
    }
}
