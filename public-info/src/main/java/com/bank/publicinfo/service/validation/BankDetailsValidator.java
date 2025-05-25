package com.bank.publicinfo.service.validation;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.exception.ValidationException;
import com.bank.publicinfo.repository.BankDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Валидатор для банковских реквизитов.
 */
@Component
@RequiredArgsConstructor
public class BankDetailsValidator {
    private final BankDetailsRepository repository;

    /**
     * Валидирует DTO для создания банковских реквизитов.
     * @param dto DTO для валидации
     * @throws ValidationException если валидация не пройдена
     */
    public void validateForCreate(BankDetailsDto dto) {
        validateBasicFields(dto);

        if (repository.existsByBik(dto.getBik())) {
            throw new ValidationException("Bank with BIK " + dto.getBik() + " already exists");
        }
        if (repository.existsByInn(dto.getInn())) {
            throw new ValidationException("Bank with INN " + dto.getInn() + " already exists");
        }
    }

    /**
     * Валидирует DTO для обновления банковских реквизитов.
     * @param existing существующая сущность
     * @param dto DTO с обновленными данными
     * @throws ValidationException если валидация не пройдена
     */
    public void validateForUpdate(BankDetails existing, BankDetailsDto dto) {
        validateBasicFields(dto);

        if (!existing.getBik().equals(dto.getBik()) && repository.existsByBik(dto.getBik())) {
            throw new ValidationException("BIK " + dto.getBik() + " belongs to another bank");
        }
        if (!existing.getInn().equals(dto.getInn()) && repository.existsByInn(dto.getInn())) {
            throw new ValidationException("INN " + dto.getInn() + " belongs to another bank");
        }
    }

    /**
     * Проверяет базовые поля DTO.
     * @param dto DTO для проверки
     * @throws ValidationException если проверка не пройдена
     */
    private void validateBasicFields(BankDetailsDto dto) {
        Objects.requireNonNull(dto, "BankDetailsDto cannot be null");
        Objects.requireNonNull(dto.getBik(), "BIK cannot be null");
        Objects.requireNonNull(dto.getInn(), "INN cannot be null");
    }
}