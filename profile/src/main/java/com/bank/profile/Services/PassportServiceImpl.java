package com.bank.profile.Services;

import com.bank.profile.DTO.PassportDto;
import com.bank.profile.Entities.Passport;
import com.bank.profile.Mappers.PassportMapper;
import com.bank.profile.Repositories.PassportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса PassportService
 */
@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {

    private final PassportRepository passportRepository;
    private final PassportMapper passportMapper;

    /**
     * Метод для создания записи
     */
    @Override
    public Passport createPassport(PassportDto passportDto) {
        final Passport passport = passportMapper.passportDtoToPassport(passportDto);
        return passportRepository.save(passport);
    }

    /**
     * Метод для обновления существующих записей
     */
    @Override
    public Passport updateProfile(PassportDto passportDto) {
        final Passport passport = passportMapper.passportDtoToPassport(passportDto);
        return passportRepository.save(passport);
    }

    /**
     * Метод для удаления записей
     */
    @Override
    public void deletePassport(Long passportId) {
        final Passport passport = passportRepository.getById(passportId);
        passportRepository.delete(passport);
    }

    /**
     * Метод для получения записей
     */
    @Override
    public PassportDto getPassport(Long passportId) {
        final Passport passport = passportRepository.getById(passportId);
        return passportMapper.passportToPassportDto(passport);
    }
}
