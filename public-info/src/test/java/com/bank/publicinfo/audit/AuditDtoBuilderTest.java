package com.bank.publicinfo.audit;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import com.bank.publicinfo.util.SecurityContextHelper;
import com.bank.publicinfo.util.StateSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link AuditDtoBuilder}.
 */
@ExtendWith(MockitoExtension.class)
class AuditDtoBuilderTest {

    /**
     * Мок для работы с контекстом безопасности.
     */
    @Mock
    private SecurityContextHelper securityContext;

    /**
     * Мок для сериализации состояния сущностей.
     */
    @Mock
    private StateSerializer serializer;

    /**
     * Тестируемый экземпляр AuditDtoBuilder с внедренными моками.
     */
    @InjectMocks
    private AuditDtoBuilder auditDtoBuilder;

    /**
     * Тестовый экземпляр BankDetails с актуальными данными.
     */
    private BankDetails testBankDetails;

    /**
     * Тестовый экземпляр BankDetails с предыдущими данными.
     */
    private BankDetails oldBankDetails;

    /**
     * Инициализирует тестовые данные перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        testBankDetails = TestUtil.createTestBankDetails();
        oldBankDetails = TestUtil.createOldBankDetails();
    }

    /**
     * Подготавливает общие моки для тестов.
     */
    private void prepareCommonMocks() {
        when(securityContext.getCurrentUser()).thenReturn(TestConstants.TEST_USER);
        when(serializer.serializeState(any(BankDetails.class))).thenReturn(TestConstants.SERIALIZED_BANK_DETAILS);
    }

    /**
     * Проверяет корректность создания AuditDto для действия CREATE.
     */
    @Test
    void build_ShouldCreateCorrectAuditDtoForCreateAction() {
        prepareCommonMocks();

        AuditDto result = auditDtoBuilder.build(ActionType.CREATE, testBankDetails, null);

        assertNotNull(result);
        assertEquals(TestConstants.ENTITY_TYPE, result.getEntityType());
        assertEquals(TestConstants.CREATE_OPERATION, result.getOperationType());
        assertEquals(TestConstants.TEST_USER, result.getCreatedBy());
        assertEquals(TestConstants.TEST_USER, result.getModifiedBy());
        assertNotNull(result.getCreatedAt());
        assertEquals(TestConstants.SERIALIZED_BANK_DETAILS, result.getEntityJson());
        assertNull(result.getNewEntityJson());
    }

    /**
     * Проверяет корректность создания AuditDto для действия UPDATE.
     */
    @Test
    void build_ShouldCreateCorrectAuditDtoForUpdateAction() {
        prepareCommonMocks();

        AuditDto result = auditDtoBuilder.build(ActionType.UPDATE, testBankDetails, oldBankDetails);

        assertNotNull(result);
        assertEquals(TestConstants.ENTITY_TYPE, result.getEntityType());
        assertEquals(TestConstants.UPDATE_OPERATION, result.getOperationType());
        assertEquals(TestConstants.TEST_USER, result.getCreatedBy());
        assertEquals(TestConstants.TEST_USER, result.getModifiedBy());
        assertNotNull(result.getCreatedAt());
        assertEquals(TestConstants.SERIALIZED_BANK_DETAILS, result.getEntityJson());
        assertEquals(TestConstants.SERIALIZED_BANK_DETAILS, result.getNewEntityJson());
    }

    /**
     * Проверяет корректность создания AuditDto для действия DELETE.
     */
    @Test
    void build_ShouldCreateCorrectAuditDtoForDeleteAction() {
        prepareCommonMocks();

        AuditDto result = auditDtoBuilder.build(ActionType.DELETE, testBankDetails, oldBankDetails);

        assertNotNull(result);
        assertEquals(TestConstants.ENTITY_TYPE, result.getEntityType());
        assertEquals(TestConstants.DELETE_OPERATION, result.getOperationType());
        assertEquals(TestConstants.TEST_USER, result.getCreatedBy());
        assertEquals(TestConstants.TEST_USER, result.getModifiedBy());
        assertNotNull(result.getCreatedAt());
        assertEquals(TestConstants.SERIALIZED_BANK_DETAILS, result.getEntityJson());
        assertNull(result.getNewEntityJson());
    }

    /**
     * Проверяет выброс исключения при передаче null в качестве нового состояния.
     */
    @Test
    void build_ShouldThrowIllegalArgumentException_WhenNewStateIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> auditDtoBuilder.build(ActionType.CREATE, null, oldBankDetails)
        );

        assertEquals("New state cannot be null", exception.getMessage());
        verifyNoInteractions(securityContext);
        verifyNoInteractions(serializer);
    }

    /**
     * Проверяет, что временные метки created и modified совпадают.
     */
    @Test
    void build_ShouldSetSameTimestampsForAllFields() {
        prepareCommonMocks();

        AuditDto result = auditDtoBuilder.build(ActionType.CREATE, testBankDetails, null);

        assertEquals(result.getCreatedAt(), result.getModifiedAt());
    }

    /**
     * Проверяет корректность вызовов сериализатора для действия CREATE.
     */
    @Test
    void build_ShouldCallSerializerCorrectlyForCreate() {
        prepareCommonMocks();

        auditDtoBuilder.build(ActionType.CREATE, testBankDetails, null);

        verify(serializer).serializeState(testBankDetails);
        verify(serializer, never()).serializeState(oldBankDetails);
    }

    /**
     * Проверяет корректность вызовов сериализатора для действия UPDATE.
     */
    @Test
    void build_ShouldCallSerializerCorrectlyForUpdate() {
        prepareCommonMocks();

        auditDtoBuilder.build(ActionType.UPDATE, testBankDetails, oldBankDetails);

        verify(serializer).serializeState(oldBankDetails);
        verify(serializer).serializeState(testBankDetails);
    }

    /**
     * Проверяет корректность вызовов сериализатора для действия DELETE.
     */
    @Test
    void build_ShouldCallSerializerCorrectlyForDelete() {
        prepareCommonMocks();

        auditDtoBuilder.build(ActionType.DELETE, testBankDetails, oldBankDetails);

        verify(serializer).serializeState(oldBankDetails);
        verify(serializer, never()).serializeState(testBankDetails);
    }
}
