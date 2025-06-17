package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.service.validation.BankDetailsValidator;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link BankDetailsServiceImpl}.
 * Проверяет функциональность сервиса банковских реквизитов.
 */
@ExtendWith(MockitoExtension.class)
class BankDetailsServiceImplTest {

    @Mock
    private BankDetailsRepository repository;

    @Mock
    private BankDetailsMapper mapper;

    @Mock
    private BankDetailsValidator validator;

    @InjectMocks
    private BankDetailsServiceImpl service;

    private BankDetailsDto dto;
    private BankDetails entity;

    /**
     * Инициализирует тестовые данные перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        dto = TestUtil.createTestBankDetailsDto();
        entity = TestUtil.createTestBankDetails();
    }

    /**
     * Проверяет создание банковских реквизитов:
     */
    @Test
    void create_ValidDto_ReturnsDto() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.create(dto);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(validator).validateForCreate(dto);
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
    }

    /**
     * Проверяет обновление банковских реквизитов:
     */
    @Test
    void update_ValidIdAndDto_ReturnsUpdatedDto() {
        when(repository.findById(TestConstants.TEST_ID)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.update(TestConstants.TEST_ID, dto);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(validator).validateForUpdate(entity, dto);
        verify(mapper).updateEntity(dto, entity);
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
    }

    /**
     * Проверяет обновление с несуществующим ID:
     */
    @Test
    void update_NonExistingId_ThrowsEntityNotFoundException() {
        when(repository.findById(TestConstants.TEST_ID_2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(TestConstants.TEST_ID_2, dto));
        verify(repository).findById(TestConstants.TEST_ID_2);
        verifyNoMoreInteractions(validator, mapper, repository);
    }

    /**
     * Проверяет получение реквизитов по ID:
     */
    @Test
    void getById_ValidId_ReturnsDto() {
        when(repository.findById(TestConstants.TEST_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        BankDetailsDto result = service.getById(TestConstants.TEST_ID);

        assertNotNull(result);
        assertEquals(dto, result);
        verify(repository).findById(TestConstants.TEST_ID);
        verify(mapper).toDto(entity);
    }

    /**
     * Проверяет получение с несуществующим ID:
     */
    @Test
    void getById_NonExistingId_ThrowsEntityNotFoundException() {
        when(repository.findById(TestConstants.TEST_ID_2)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getById(TestConstants.TEST_ID_2));
        verify(repository).findById(TestConstants.TEST_ID_2);
        verifyNoMoreInteractions(mapper);
    }

    /**
     * Проверяет удаление реквизитов:
     */
    @Test
    void delete_ValidId_DeletesEntity() {
        when(repository.existsById(TestConstants.TEST_ID)).thenReturn(true);

        service.delete(TestConstants.TEST_ID);

        verify(repository).existsById(TestConstants.TEST_ID);
        verify(repository).deleteById(TestConstants.TEST_ID);
    }

    /**
     * Проверяет удаление с несуществующим ID:
     */
    @Test
    void delete_NonExistingId_ThrowsEntityNotFoundException() {
        when(repository.existsById(TestConstants.TEST_ID_2)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.delete(TestConstants.TEST_ID_2));
        verify(repository).existsById(TestConstants.TEST_ID_2);
        verifyNoMoreInteractions(repository);
    }

    /**
     * Проверяет существование реквизитов по ID:
     */
    @Test
    void existsById_ExistingId_ReturnsTrue() {
        when(repository.existsById(TestConstants.TEST_ID)).thenReturn(true);

        boolean result = service.existsById(TestConstants.TEST_ID);

        assertTrue(result);
        verify(repository).existsById(TestConstants.TEST_ID);
    }

    /**
     * Проверяет существование реквизитов по ID:
     */
    @Test
    void existsById_NonExistingId_ReturnsFalse() {
        when(repository.existsById(TestConstants.TEST_ID_2)).thenReturn(false);

        boolean result = service.existsById(TestConstants.TEST_ID_2);

        assertFalse(result);
        verify(repository).existsById(TestConstants.TEST_ID_2);
    }

    /**
     * Проверяет существование реквизитов по БИК:
     */
    @Test
    void existsByBik_ExistingBik_ReturnsTrue() {
        when(repository.existsByBik(TestConstants.TEST_BIK)).thenReturn(true);

        boolean result = service.existsByBik(TestConstants.TEST_BIK);

        assertTrue(result);
        verify(repository).existsByBik(TestConstants.TEST_BIK);
    }

    /**
     * Проверяет существование реквизитов по БИК:
     */
    @Test
    void existsByBik_NonExistingBik_ReturnsFalse() {
        when(repository.existsByBik(TestConstants.TEST_OLD_BIK)).thenReturn(false);

        boolean result = service.existsByBik(TestConstants.TEST_OLD_BIK);

        assertFalse(result);
        verify(repository).existsByBik(TestConstants.TEST_OLD_BIK);
    }

    /**
     * Проверяет валидацию null ID:
     */
    @Test
    void validateId_NullId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getById(TestConstants.NULL_ID));
        assertEquals(TestConstants.INVALID_ID_MESSAGE, exception.getMessage());
    }

    /**
     * Проверяет валидацию отрицательного ID:
     */
    @Test
    void validateId_NegativeId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getById(TestConstants.NEGATIVE_ID));
        assertEquals(TestConstants.INVALID_ID_MESSAGE, exception.getMessage());
    }

    /**
     * Проверяет валидацию нулевого ID:
     */
    @Test
    void validateId_ZeroId_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.getById(TestConstants.ZERO_ID));
        assertEquals(TestConstants.INVALID_ID_MESSAGE, exception.getMessage());
    }
}
