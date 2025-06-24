package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

/**
 * Тесты для {@link AuditMapper}.
 */
class AuditMapperTest {

    private final AuditMapper auditMapper = Mappers.getMapper(AuditMapper.class);

    /**
     * Проверяет преобразование Audit в AuditDto.
     */
    @Test
    void toDto_shouldConvertEntityToDto() {
        Audit entity = new Audit();
        entity.setId(TestConstants.TEST_ID);
        entity.setEntityType(TestConstants.ENTITY_TYPE);
        entity.setOperationType(TestConstants.CREATE_OPERATION);
        entity.setCreatedBy(TestConstants.TEST_USER);
        entity.setModifiedBy(TestConstants.TEST_USER);
        entity.setCreatedAt(TestUtil.createTestZonedDateTime());
        entity.setModifiedAt(TestUtil.createTestZonedDateTime().plus(TestConstants.MODIFIED_AT_DELTA_MINUTES, ChronoUnit.MINUTES));
        entity.setNewEntityJson(TestConstants.SERIALIZED_BANK_DETAILS);
        entity.setEntityJson(TestConstants.EMPTY_JSON);

        AuditDto dto = auditMapper.toDto(entity);

        assertThat(dto)
                .isNotNull()
                .extracting(
                        AuditDto::getId,
                        AuditDto::getEntityType,
                        AuditDto::getOperationType,
                        AuditDto::getCreatedBy,
                        AuditDto::getModifiedBy,
                        AuditDto::getCreatedAt,
                        AuditDto::getModifiedAt,
                        AuditDto::getNewEntityJson,
                        AuditDto::getEntityJson
                )
                .containsExactly(
                        entity.getId(),
                        entity.getEntityType(),
                        entity.getOperationType(),
                        entity.getCreatedBy(),
                        entity.getModifiedBy(),
                        entity.getCreatedAt(),
                        entity.getModifiedAt(),
                        entity.getNewEntityJson(),
                        entity.getEntityJson()
                );
    }

    /**
     * Проверяет преобразование AuditDto в Audit.
     */
    @Test
    void toEntity_shouldConvertDtoToEntity() {
        AuditDto dto = new AuditDto();
        dto.setId(TestConstants.TEST_ID);
        dto.setEntityType(TestConstants.LICENSE_ENTITY_TYPE);
        dto.setOperationType(TestConstants.UPDATE_OPERATION);
        dto.setCreatedBy(TestConstants.TEST_USER);
        dto.setModifiedBy(TestConstants.TEST_USER);
        dto.setCreatedAt(TestUtil.createTestZonedDateTime());
        dto.setModifiedAt(TestUtil.createTestZonedDateTime().plus(TestConstants.CREATED_AT_DELTA_MINUTES, ChronoUnit.MINUTES));
        dto.setNewEntityJson(TestConstants.LICENSE_JSON);
        dto.setEntityJson(TestConstants.OLD_LICENSE_JSON);

        Audit entity = auditMapper.toEntity(dto);

        assertThat(entity)
                .isNotNull()
                .extracting(
                        Audit::getId,
                        Audit::getEntityType,
                        Audit::getOperationType,
                        Audit::getCreatedBy,
                        Audit::getModifiedBy,
                        Audit::getCreatedAt,
                        Audit::getModifiedAt,
                        Audit::getNewEntityJson,
                        Audit::getEntityJson
                )
                .containsExactly(
                        dto.getId(),
                        dto.getEntityType(),
                        dto.getOperationType(),
                        dto.getCreatedBy(),
                        dto.getModifiedBy(),
                        dto.getCreatedAt(),
                        dto.getModifiedAt(),
                        dto.getNewEntityJson(),
                        dto.getEntityJson()
                );
    }

    /**
     * Проверяет преобразование списка Audit в список AuditDto.
     */
    @Test
    void toDtoList_shouldConvertEntityListToDtoList() {
        Audit audit1 = new Audit();
        audit1.setId(TestConstants.TEST_ID);
        audit1.setEntityType(TestConstants.TYPE1_ENTITY_TYPE);

        Audit audit2 = new Audit();
        audit2.setId(TestConstants.TEST_ID_2);
        audit2.setEntityType(TestConstants.TYPE2_ENTITY_TYPE);

        List<Audit> entities = List.of(audit1, audit2);

        List<AuditDto> dtos = auditMapper.toDtoList(entities);

        assertThat(dtos)
                .hasSize(TestConstants.TEST_LIST_SIZE)
                .extracting(TestConstants.AUDIT_DTO_ID_FIELD, TestConstants.AUDIT_DTO_ENTITY_TYPE_FIELD)
                .containsExactly(
                        tuple(TestConstants.TEST_ID, TestConstants.TYPE1_ENTITY_TYPE),
                        tuple(TestConstants.TEST_ID_2, TestConstants.TYPE2_ENTITY_TYPE)
                );
    }

    /**
     * Проверяет обработку null при преобразовании Audit в DTO.
     */
    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertThat(auditMapper.toDto(null)).isNull();
    }

    /**
     * Проверяет обработку null при преобразовании DTO в Audit.
     */
    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertThat(auditMapper.toEntity(null)).isNull();
    }

    /**
     * Проверяет обработку null при преобразовании списка.
     */
    @Test
    void toDtoList_shouldReturnNullWhenListIsNull() {
        assertThat(auditMapper.toDtoList(null)).isNull();
    }

    /**
     * Проверяет обработку пустого списка.
     */
    @Test
    void toDtoList_shouldReturnEmptyListWhenInputIsEmpty() {
        assertThat(auditMapper.toDtoList(List.of())).isEmpty();
    }
}
