package com.bank.profile.Services;

import com.bank.profile.DTO.PassportDto;
import com.bank.profile.Entities.Passport;

/**
 * Интерфейс сервиса Passport
 */
public interface PassportService {
    Passport createPassport(PassportDto passportDto);
    Passport updateProfile(PassportDto passportDto);
    void deletePassport(Long passportId);
    PassportDto getPassport(Long passportId);
}
