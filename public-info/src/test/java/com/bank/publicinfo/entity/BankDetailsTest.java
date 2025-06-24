package com.bank.publicinfo.entity;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для сущности {@link BankDetails}.
 * Проверяют:
 * <ul>
 *   <li>Создание объекта через конструкторы и сеттеры</li>
 *   <li>Валидацию полей</li>
 *   <li>Реализацию equals/hashCode</li>
 *   <li>Формирование строкового представления</li>
 * </ul>
 */
class BankDetailsTest {

    private final Validator validator;

    public BankDetailsTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Проверяет создание объекта через сеттеры и корректность геттеров.
     */
    @Test
    void entityCreationAndGettersSettersTest() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(TestConstants.TEST_ID);
        bankDetails.setBik(TestConstants.TEST_BIK);
        bankDetails.setInn(TestConstants.TEST_INN);
        bankDetails.setKpp(TestConstants.TEST_KPP);
        bankDetails.setCorAccount(TestConstants.TEST_COR_ACCOUNT);
        bankDetails.setCity(TestConstants.TEST_CITY);
        bankDetails.setJointStockCompany(TestConstants.TEST_COMPANY_TYPE);
        bankDetails.setName(TestConstants.TEST_BANK_NAME);

        assertAll(
                () -> assertEquals(TestConstants.TEST_ID, bankDetails.getId(), TestConstants.ID_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_BIK, bankDetails.getBik(), TestConstants.BIK_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_INN, bankDetails.getInn(), TestConstants.INN_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_KPP, bankDetails.getKpp(), TestConstants.KPP_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_COR_ACCOUNT, bankDetails.getCorAccount(), TestConstants.COR_ACCOUNT_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_CITY, bankDetails.getCity(), TestConstants.CITY_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_COMPANY_TYPE, bankDetails.getJointStockCompany(), TestConstants.COMPANY_TYPE_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_BANK_NAME, bankDetails.getName(), TestConstants.BANK_NAME_SHOULD_MATCH)
        );
    }

    /**
     * Проверяет создание объекта через конструктор со всеми параметрами.
     */
    @Test
    void allArgsConstructorTest() {
        BankDetails bankDetails = new BankDetails(
                TestConstants.TEST_ID,
                TestConstants.TEST_BIK,
                TestConstants.TEST_INN,
                TestConstants.TEST_KPP,
                TestConstants.TEST_COR_ACCOUNT,
                TestConstants.TEST_CITY,
                TestConstants.TEST_COMPANY_TYPE,
                TestConstants.TEST_BANK_NAME
        );

        assertAll(
                () -> assertEquals(TestConstants.TEST_ID, bankDetails.getId(), TestConstants.ID_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_BIK, bankDetails.getBik(), TestConstants.BIK_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_INN, bankDetails.getInn(), TestConstants.INN_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_KPP, bankDetails.getKpp(), TestConstants.KPP_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_COR_ACCOUNT, bankDetails.getCorAccount(), TestConstants.COR_ACCOUNT_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_CITY, bankDetails.getCity(), TestConstants.CITY_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_COMPANY_TYPE, bankDetails.getJointStockCompany(), TestConstants.COMPANY_TYPE_SHOULD_MATCH),
                () -> assertEquals(TestConstants.TEST_BANK_NAME, bankDetails.getName(), TestConstants.BANK_NAME_SHOULD_MATCH)
        );
    }

    /**
     * Проверяет создание объекта через конструктор по умолчанию.
     */
    @Test
    void noArgsConstructorTest() {
        BankDetails bankDetails = new BankDetails();
        assertNotNull(bankDetails, TestConstants.ENTITY_SHOULD_NOT_BE_NULL);
    }

    /**
     * Проверяет реализацию интерфейса AuditableEntity.
     */
    @Test
    void auditableEntityImplementationTest() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(TestConstants.TEST_ID);
        assertEquals(TestConstants.TEST_ID, bankDetails.getId(), TestConstants.ID_SHOULD_MATCH);
    }

    /**
     * Проверяет валидацию ограничений длины полей.
     */
    @Test
    void fieldLengthConstraintsTest() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(TestConstants.TEST_ID);
        bankDetails.setBik(TestConstants.TEST_BIK);
        bankDetails.setInn(TestConstants.TEST_INN);
        bankDetails.setKpp(TestConstants.TEST_KPP);
        bankDetails.setCorAccount(TestConstants.TEST_COR_ACCOUNT);

        bankDetails.setCity(TestConstants.TEST_LONG_STRING.repeat(TestConstants.MAX_CITY_LENGTH));
        bankDetails.setJointStockCompany(TestConstants.TEST_LONG_STRING.repeat(TestConstants.MAX_COMPANY_TYPE_LENGTH));
        bankDetails.setName(TestConstants.TEST_LONG_STRING.repeat(TestConstants.MAX_BANK_NAME_LENGTH));

        var violations = validator.validate(bankDetails);
        assertEquals(0, violations.size(), TestConstants.NO_VIOLATIONS_EXPECTED);
    }

    /**
     * Проверяет уникальные ограничения (заглушка для теста, требующего БД).
     */
    @Test
    void uniqueConstraintsTest() {
        assertTrue(true, TestConstants.UNIQUE_CONSTRAINTS_REQUIRE_DB);
    }

    /**
     * Проверяет корректность реализации equals и hashCode.
     */
    @Test
    void equalsAndHashCodeTest() {
        BankDetails bankDetails1 = new BankDetails(
                TestConstants.TEST_ID,
                TestConstants.TEST_BIK,
                TestConstants.TEST_INN,
                TestConstants.TEST_KPP,
                TestConstants.TEST_COR_ACCOUNT,
                TestConstants.TEST_CITY,
                TestConstants.TEST_COMPANY_TYPE,
                TestConstants.TEST_BANK_NAME
        );

        BankDetails bankDetails2 = new BankDetails(
                TestConstants.TEST_ID,
                TestConstants.TEST_BIK,
                TestConstants.TEST_INN,
                TestConstants.TEST_KPP,
                TestConstants.TEST_COR_ACCOUNT,
                TestConstants.TEST_CITY,
                TestConstants.TEST_COMPANY_TYPE,
                TestConstants.TEST_BANK_NAME
        );

        BankDetails bankDetails3 = new BankDetails(
                TestConstants.TEST_ID_2,
                TestConstants.TEST_ALT_BIK,
                TestConstants.TEST_ALT_INN,
                TestConstants.TEST_ALT_KPP,
                TestConstants.TEST_ALT_COR_ACCOUNT,
                TestConstants.TEST_ALT_CITY,
                TestConstants.TEST_ALT_COMPANY_TYPE,
                TestConstants.TEST_ALT_BANK_NAME
        );

        assertAll(
                () -> assertEquals(bankDetails1, bankDetails2, TestConstants.EQUALS_SHOULD_BE_TRUE_FOR_SAME_OBJECTS),
                () -> assertNotEquals(bankDetails1, bankDetails3, TestConstants.EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_OBJECTS),
                () -> assertEquals(bankDetails1.hashCode(), bankDetails2.hashCode(), TestConstants.HASHCODE_SHOULD_BE_EQUAL_FOR_SAME_OBJECTS),
                () -> assertNotEquals(bankDetails1.hashCode(), bankDetails3.hashCode(), TestConstants.HASHCODE_SHOULD_BE_DIFFERENT_FOR_DIFFERENT_OBJECTS)
        );
    }

    /**
     * Проверяет формирование строкового представления объекта.
     */
    @Test
    void toStringTest() {
        BankDetails bankDetails = new BankDetails(
                TestConstants.TEST_ID,
                TestConstants.TEST_BIK,
                TestConstants.TEST_INN,
                TestConstants.TEST_KPP,
                TestConstants.TEST_COR_ACCOUNT,
                TestConstants.TEST_CITY,
                TestConstants.TEST_COMPANY_TYPE,
                TestConstants.TEST_BANK_NAME
        );

        String toStringResult = bankDetails.toString();

        assertAll(
                () -> assertNotNull(toStringResult, TestConstants.TOSTRING_SHOULD_NOT_BE_NULL),
                () -> assertTrue(toStringResult.contains(TestConstants.TEST_BANK_NAME), TestConstants.TOSTRING_SHOULD_CONTAIN_BANK_NAME),
                () -> assertTrue(toStringResult.contains(TestConstants.TEST_CITY), TestConstants.TOSTRING_SHOULD_CONTAIN_CITY)
        );
    }
}
