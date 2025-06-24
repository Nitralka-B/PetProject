package com.bank.publicinfo.entity;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для проверки сущности {@link Audit}.
 * Проверяет JPA-аннотации, настройки полей и Lombok-функциональность.
 */
class AuditTest {

    /**
     * Проверяет наличие аннотаций {@link Entity} и {@link Table} у класса Audit.
     * Убеждается, что имя таблицы соответствует "audit".
     */
    @Test
    void AuditEntityAnnotationsTest() {
        assertTrue(Audit.class.isAnnotationPresent(Entity.class),
                TestConstants.ENTITY_ANNOTATION_SHOULD_BE_PRESENT);

        Table tableAnnotation = Audit.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation,
                TestConstants.TABLE_ANNOTATION_SHOULD_NOT_BE_NULL);
        assertEquals(TestConstants.AUDIT_TABLE_NAME, tableAnnotation.name(),
                TestConstants.TABLE_NAME_SHOULD_MATCH_AUDIT);
    }

    /**
     * Проверяет настройки поля id:
     */
    @Test
    void IdFieldTest() throws NoSuchFieldException {
        Field idField = Audit.class.getDeclaredField(TestConstants.ID_FIELD_NAME);
        assertTrue(idField.isAnnotationPresent(Id.class),
                TestConstants.ID_ANNOTATION_SHOULD_BE_PRESENT);

        GeneratedValue generatedValue = idField.getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValue,
                TestConstants.GENERATED_VALUE_ANNOTATION_SHOULD_NOT_BE_NULL);
        assertEquals(GenerationType.IDENTITY, generatedValue.strategy(),
                TestConstants.GENERATION_STRATEGY_SHOULD_BE_IDENTITY);
    }

    /**
     * Проверяет аннотации {@link Column} для полей сущности.
     *
     * @throws Exception если поля не найдены
     */
    @Test
    void ColumnAnnotationsTest() throws Exception {
        ColumnAnnotationTest(TestConstants.ENTITY_TYPE_FIELD_NAME, false);
        ColumnAnnotationTest(TestConstants.OPERATION_TYPE_FIELD_NAME, false);
        ColumnAnnotationTest(TestConstants.CREATED_BY_FIELD_NAME, false);
        ColumnAnnotationTest(TestConstants.MODIFIED_BY_FIELD_NAME, true);
        ColumnAnnotationTest(TestConstants.CREATED_AT_FIELD_NAME, false);
        ColumnAnnotationTest(TestConstants.MODIFIED_AT_FIELD_NAME, true);
        ColumnAnnotationTest(TestConstants.NEW_ENTITY_JSON_FIELD_NAME, true);
        ColumnAnnotationTest(TestConstants.ENTITY_JSON_FIELD_NAME, false);
    }

    /**
     * Проверяет настройку nullable для поля.
     *
     * @param fieldName        имя поля
     * @param shouldBeNullable должен ли быть nullable
     * @throws Exception если поле не найдено
     */
    private void ColumnAnnotationTest(String fieldName, boolean shouldBeNullable) throws Exception {
        Field field = Audit.class.getDeclaredField(fieldName);
        Column column = field.getAnnotation(Column.class);
        assertNotNull(column,
                String.format(TestConstants.COLUMN_ANNOTATION_SHOULD_NOT_BE_NULL_FORMAT, fieldName));
        assertEquals(shouldBeNullable, column.nullable(),
                String.format(TestConstants.FIELD_NULLABLE_SHOULD_MATCH_FORMAT,
                        fieldName, shouldBeNullable));
    }

    /**
     * Проверяет Lombok-функциональность:
     */
    @Test
    void LombokFunctionalityTest() {
        ZonedDateTime now = ZonedDateTime.now();
        Audit audit = new Audit(
                TestConstants.TEST_ID,
                TestConstants.ENTITY_TYPE,
                TestConstants.CREATE_OPERATION,
                TestConstants.TEST_USER,
                TestConstants.SYSTEM_USER,
                now,
                now,
                TestConstants.EMPTY_JSON,
                TestConstants.EMPTY_JSON
        );

        assertNotNull(audit.toString(),
                TestConstants.TOSTRING_SHOULD_NOT_BE_NULL);
        assertEquals(TestConstants.TEST_ID, audit.getId(),
                TestConstants.ID_SHOULD_MATCH);
        assertEquals(TestConstants.ENTITY_TYPE, audit.getEntityType(),
                TestConstants.ENTITY_TYPE_SHOULD_MATCH);
        assertEquals(TestConstants.CREATE_OPERATION, audit.getOperationType(),
                TestConstants.OPERATION_TYPE_SHOULD_MATCH);
        assertEquals(TestConstants.TEST_USER, audit.getCreatedBy(),
                TestConstants.CREATED_BY_SHOULD_MATCH);
        assertEquals(TestConstants.SYSTEM_USER, audit.getModifiedBy(),
                TestConstants.MODIFIED_BY_SHOULD_MATCH);
        assertEquals(now, audit.getCreatedAt(),
                TestConstants.CREATED_AT_SHOULD_MATCH);
        assertEquals(now, audit.getModifiedAt(),
                TestConstants.MODIFIED_AT_SHOULD_MATCH);
        assertEquals(TestConstants.EMPTY_JSON, audit.getNewEntityJson(),
                TestConstants.NEW_ENTITY_JSON_SHOULD_MATCH);
        assertEquals(TestConstants.EMPTY_JSON, audit.getEntityJson(),
                TestConstants.ENTITY_JSON_SHOULD_MATCH);
    }

    /**
     * Проверяет контракты equals() и hashCode():
     */
    @Test
    void EqualsAndHashCodeTest() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime plusOneHour = now.plusHours(1);

        Audit audit1 = new Audit(
                TestConstants.TEST_ID,
                TestConstants.TYPE1_ENTITY_TYPE,
                TestConstants.CREATE_OPERATION,
                TestConstants.TEST_USER,
                TestConstants.SYSTEM_USER,
                now,
                now,
                TestConstants.EMPTY_JSON,
                TestConstants.EMPTY_JSON
        );

        Audit audit2 = new Audit(
                TestConstants.TEST_ID,
                TestConstants.TYPE1_ENTITY_TYPE,
                TestConstants.CREATE_OPERATION,
                TestConstants.TEST_USER,
                TestConstants.SYSTEM_USER,
                now,
                now,
                TestConstants.EMPTY_JSON,
                TestConstants.EMPTY_JSON
        );

        Audit differentAudit = new Audit(
                TestConstants.TEST_ID_2,
                TestConstants.TYPE2_ENTITY_TYPE,
                TestConstants.UPDATE_OPERATION,
                TestConstants.OTHER_USER,
                TestConstants.OTHER_MODIFIER,
                plusOneHour,
                plusOneHour,
                TestConstants.LICENSE_JSON,
                TestConstants.LICENSE_JSON
        );

        assertEquals(audit1, audit2,
                TestConstants.EQUALS_SHOULD_BE_TRUE_FOR_SAME_OBJECTS);
        assertNotEquals(audit1, differentAudit,
                TestConstants.EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_OBJECTS);
        assertNotEquals(audit2, differentAudit,
                TestConstants.EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_OBJECTS);
        assertEquals(audit1.hashCode(), audit2.hashCode(),
                TestConstants.HASHCODE_SHOULD_BE_EQUAL_FOR_SAME_OBJECTS);
        assertNotEquals(audit1.hashCode(), differentAudit.hashCode(),
                TestConstants.HASHCODE_SHOULD_BE_DIFFERENT_FOR_DIFFERENT_OBJECTS);
        assertNotEquals(audit1, null,
                TestConstants.EQUALS_SHOULD_BE_FALSE_FOR_NULL);
        assertNotEquals(audit1, new Object(),
                TestConstants.EQUALS_SHOULD_BE_FALSE_FOR_DIFFERENT_CLASS);
        assertEquals(audit1, audit1,
                TestConstants.EQUALS_SHOULD_BE_REFLEXIVE);

        Audit audit3 = new Audit(
                TestConstants.TEST_ID,
                TestConstants.TYPE1_ENTITY_TYPE,
                TestConstants.CREATE_OPERATION,
                TestConstants.TEST_USER,
                TestConstants.SYSTEM_USER,
                now,
                now,
                TestConstants.EMPTY_JSON,
                TestConstants.EMPTY_JSON
        );
        assertEquals(audit1, audit2,
                TestConstants.EQUALS_SHOULD_BE_SYMMETRIC);
        assertEquals(audit2, audit3,
                TestConstants.EQUALS_SHOULD_BE_TRANSITIVE);
        assertEquals(audit1, audit3,
                TestConstants.EQUALS_SHOULD_BE_TRANSITIVE);

        assertEquals(plusOneHour, differentAudit.getCreatedAt(),
                TestConstants.TIME_SHOULD_BE_PLUS_ONE_HOUR);
        assertEquals(plusOneHour, differentAudit.getModifiedAt(),
                TestConstants.TIME_SHOULD_BE_PLUS_ONE_HOUR);
    }
}
