package com.bank.publicinfo.testutil;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.audit.Auditable;
import com.bank.publicinfo.audit.ActionType;
import com.bank.publicinfo.entity.BankDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.mockito.Mockito;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Утилитарный класс для создания тестовых данных и mock-объектов.
 */
public final class TestUtil {

    /**
     * Создает mock-объект {@link Auditable} с заданными параметрами.
     */
    public static Auditable createAuditable(ActionType actionType, boolean saveOldState) {
        Auditable auditable = Mockito.mock(Auditable.class);
        Mockito.lenient().when(auditable.actionType()).thenReturn(actionType);
        Mockito.lenient().when(auditable.saveOldState()).thenReturn(saveOldState);
        return auditable;
    }

    /**
     * Создает mock-объект {@link JoinPoint} с подписью метода.
     */
    public static JoinPoint mockJoinPointWithSignature() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        Mockito.lenient().when(joinPoint.getSignature()).thenAnswer(invocation -> signature);
        Mockito.lenient().when(signature.toShortString()).thenAnswer(invocation -> TestConstants.TEST_METHOD_SIGNATURE);
        return joinPoint;
    }

    /**
     * Тестовая сущность, реализующая {@link AuditableEntity}.
     */
    public static class TestEntity implements AuditableEntity {
        @Override
        public Long getId() {
            return TestConstants.TEST_ID;
        }
    }

    /**
     * Создает тестовый объект {@link BankDetails} с предустановленными значениями.
     */
    public static BankDetails createTestBankDetails() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(TestConstants.TEST_ID);
        bankDetails.setBik(TestConstants.TEST_BIK);
        bankDetails.setInn(TestConstants.TEST_INN);
        bankDetails.setKpp(TestConstants.TEST_KPP);
        bankDetails.setCorAccount(TestConstants.TEST_COR_ACCOUNT);
        bankDetails.setCity(TestConstants.TEST_CITY);
        bankDetails.setJointStockCompany(TestConstants.TEST_COMPANY_TYPE);
        bankDetails.setName(TestConstants.TEST_BANK_NAME);
        return bankDetails;
    }

    /**
     * Создает тестовый объект {@link BankDetails} с "старыми" значениями.
     */
    public static BankDetails createOldBankDetails() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(TestConstants.TEST_ID);
        bankDetails.setBik(TestConstants.TEST_OLD_BIK);
        return bankDetails;
    }

    /**
     * Вложенный класс с утилитами для тестирования исключений.
     */
    public final class ExceptionTestUtil {
        /**
         * Проверяет базовые свойства исключения.
         */
        public static void assertBasicExceptionProperties(Exception exception, String expectedMessage) {
            assertNotNull(exception);
            assertEquals(expectedMessage, exception.getMessage());
            assertTrue(exception instanceof RuntimeException);
        }
    }

    /**
     * Создает новый {@link BankDetails} с минимальными данными.
     */
    public static BankDetails createNewBankDetails() {
        BankDetails details = new BankDetails();
        details.setInn(TestConstants.TEST_INN);
        return details;
    }

    /**
     * Создает тестовый {@link BankDetailsDto} с предустановленными значениями.
     */
    public static BankDetailsDto createTestBankDetailsDto() {
        BankDetailsDto dto = new BankDetailsDto();
        dto.setId(TestConstants.TEST_ID);
        dto.setBik(TestConstants.TEST_BIK);
        dto.setInn(TestConstants.TEST_INN);
        dto.setKpp(TestConstants.TEST_KPP);
        dto.setCorAccount(TestConstants.TEST_COR_ACCOUNT);
        dto.setCity(TestConstants.TEST_CITY);
        dto.setJointStockCompany(TestConstants.TEST_COMPANY_TYPE);
        dto.setName(TestConstants.TEST_BANK_NAME);
        return dto;
    }

    /**
     * Создает тестовую дату-время в системном часовом поясе.
     */
    public static ZonedDateTime createTestZonedDateTime() {
        return ZonedDateTime.of(2023, 1, 1, 12, 0, 0, 0, ZoneId.systemDefault());
    }

    /**
     * Создает тестовый {@link AuditDto}.
     */
    public static AuditDto createTestAuditDto() {
        AuditDto dto = new AuditDto();
        dto.setId(TestConstants.TEST_ID);
        dto.setEntityType(TestConstants.ENTITY_TYPE);
        dto.setOperationType(TestConstants.CREATE_OPERATION);
        dto.setCreatedBy(TestConstants.TEST_USER);
        dto.setModifiedBy(TestConstants.TEST_USER);
        dto.setCreatedAt(createTestZonedDateTime());
        dto.setModifiedAt(createTestZonedDateTime().plusMinutes(30));
        dto.setNewEntityJson(TestConstants.SERIALIZED_BANK_DETAILS);
        dto.setEntityJson(TestConstants.EMPTY_JSON);
        return dto;
    }

    /**
     * Создает тестовую сущность {@link Audit}.
     */
    public static Audit createTestAuditEntity() {
        Audit entity = new Audit();
        entity.setId(TestConstants.TEST_ID);
        entity.setEntityType(TestConstants.ENTITY_TYPE);
        entity.setOperationType(TestConstants.CREATE_OPERATION);
        entity.setCreatedBy(TestConstants.TEST_USER);
        entity.setModifiedBy(TestConstants.TEST_USER);
        entity.setCreatedAt(createTestZonedDateTime());
        entity.setModifiedAt(createTestZonedDateTime().plusMinutes(30));
        entity.setNewEntityJson(TestConstants.SERIALIZED_BANK_DETAILS);
        entity.setEntityJson(TestConstants.EMPTY_JSON);
        return entity;
    }
    /**
     * Тестовый класс для проверки сериализации.
     */
    public static class TestObject {
        public String field;
        public TestObject(String field) {
            this.field = field;
        }
    }
}
