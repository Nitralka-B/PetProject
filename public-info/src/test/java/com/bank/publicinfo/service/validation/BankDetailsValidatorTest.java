package com.bank.publicinfo.service.validation;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.exception.ValidationException;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Тесты валидации {@link BankDetailsValidator}.
 * Проверяют корректность валидации банковских реквизитов:
 */
@ExtendWith(MockitoExtension.class)
class BankDetailsValidatorTest {

    /** Мок репозитория для проверки существующих реквизитов */
    @Mock
    private BankDetailsRepository repository;

    /** Тестируемый валидатор банковских реквизитов */
    @InjectMocks
    private BankDetailsValidator validator;

    /**
     * Проверяет валидацию при создании с существующим БИК.
     */
    @Test
    void validateForCreate_ShouldThrowWhenBikExists() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        when(repository.existsByBik(TestConstants.TEST_BIK)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertEquals(String.format(TestConstants.BIK_ALREADY_EXISTS_MESSAGE, TestConstants.TEST_BIK),
                exception.getMessage());
    }

    /**
     * Проверяет валидацию при создании с существующим ИНН.
     */
    @Test
    void validateForCreate_ShouldThrowWhenInnExists() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        when(repository.existsByInn(TestConstants.TEST_INN)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForCreate(dto));

        assertEquals(String.format(TestConstants.INN_ALREADY_EXISTS_MESSAGE, TestConstants.TEST_INN),
                exception.getMessage());
    }

    /**
     * Проверяет успешную валидацию при создании с уникальными реквизитами.
     */
    @Test
    void validateForCreate_ShouldPassWhenNoDuplicates() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        when(repository.existsByBik(TestConstants.TEST_BIK)).thenReturn(false);
        when(repository.existsByInn(TestConstants.TEST_INN)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateForCreate(dto));
    }

    /**
     * Проверяет валидацию при изменении БИК на уже существующий.
     */
    @Test
    void validateForUpdate_ShouldThrowWhenBikChangedAndExists() {
        BankDetails existing = TestUtil.createTestBankDetails();
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        dto.setBik(TestConstants.TEST_OLD_BIK);
        when(repository.existsByBik(TestConstants.TEST_OLD_BIK)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForUpdate(existing, dto));

        assertEquals(String.format(TestConstants.BIK_BELONGS_TO_ANOTHER_MESSAGE, TestConstants.TEST_OLD_BIK),
                exception.getMessage());
    }

    /**
     * Проверяет валидацию при изменении ИНН на уже существующий.
     */
    @Test
    void validateForUpdate_ShouldThrowWhenInnChangedAndExists() {
        BankDetails existing = TestUtil.createTestBankDetails();
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        dto.setInn(TestConstants.TEST_OLD_INN);
        when(repository.existsByInn(TestConstants.TEST_OLD_INN)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validator.validateForUpdate(existing, dto));

        assertEquals(String.format(TestConstants.INN_BELONGS_TO_ANOTHER_MESSAGE, TestConstants.TEST_OLD_INN),
                exception.getMessage());
    }

    /**
     * Проверяет успешную валидацию при обновлении без конфликтов.
     */
    @Test
    void validateForUpdate_ShouldPassWhenNoConflicts() {
        BankDetails existing = TestUtil.createTestBankDetails();
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();

        assertDoesNotThrow(() -> validator.validateForUpdate(existing, dto));
    }

    /**
     * Проверяет валидацию при null DTO.
     */
    @Test
    void validateBasicFields_ShouldThrowWhenDtoIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> validator.validateForCreate(null));

        assertEquals(TestConstants.DTO_CANNOT_BE_NULL_MESSAGE, exception.getMessage());
    }

    /**
     * Проверяет валидацию при null БИК.
     */
    @Test
    void validateBasicFields_ShouldThrowWhenBikIsNull() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        dto.setBik(null);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> validator.validateForCreate(dto));

        assertEquals(TestConstants.BIK_CANNOT_BE_NULL_MESSAGE, exception.getMessage());
    }

    /**
     * Проверяет валидацию при null ИНН.
     */
    @Test
    void validateBasicFields_ShouldThrowWhenInnIsNull() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();
        dto.setInn(null);

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> validator.validateForCreate(dto));

        assertEquals(TestConstants.INN_CANNOT_BE_NULL_MESSAGE, exception.getMessage());
    }
}
