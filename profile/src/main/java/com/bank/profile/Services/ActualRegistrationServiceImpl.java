package com.bank.profile.Services;

import com.bank.profile.DTO.ActualRegistrationDto;
import com.bank.profile.Entities.ActualRegistration;
import com.bank.profile.Mappers.ActualRegistrationMapper;
import com.bank.profile.Repositories.ActualRegistrationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса ActualRegistrationService
 */
@Service
@RequiredArgsConstructor
public class ActualRegistrationServiceImpl implements ActualRegistrationService {

    private final ActualRegistrationRepository actualRegistrationRepository;
    private final ActualRegistrationMapper actualRegistrationMapper;

    /**
     * Метод отвечающий за создание записи
     */
    @Transactional
    @Override
    public ActualRegistration createActualRegistration(ActualRegistrationDto actualRegistrationDto) {
        final ActualRegistration actualRegistration =
                actualRegistrationMapper.toActualRegistration(actualRegistrationDto);
        return actualRegistrationRepository.save(actualRegistration);
    }

    /**
     * Метод отвечающий за обновление записей
     */
    @Transactional
    @Override
    public ActualRegistration updateActualRegistration(ActualRegistrationDto actualRegistrationDto) {
        final ActualRegistration actualRegistration =
                actualRegistrationMapper.toActualRegistration(actualRegistrationDto);
        return actualRegistrationRepository.save(actualRegistration);
    }

    /**
     * Метод отвечающий за удаление записей
     */
    @Transactional
    @Override
    public void deleteActualRegistration(Long actualRegistrationId) {
        final ActualRegistration actualRegistration =
                actualRegistrationRepository.getById(actualRegistrationId);
        actualRegistrationRepository.delete(actualRegistration);
    }

    /**
     * Метод отвечающий за получение записей
     */
    @Override
    public ActualRegistration getActualRegistration(Long actualRegistrationId) {
        return actualRegistrationRepository.getById(actualRegistrationId);
    }
}
