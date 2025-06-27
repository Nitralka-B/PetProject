package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entities.Audit;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit-tests for {@link AuditMapper}
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuditMapperTest {

    private static final long ID_1 = 1L;
    private static final long ID_999 = 999L;
    private static final String ENTITY_TYPE_CARD = "SuspiciousCardTransfer";
    private static final String ENTITY_TYPE_PHONE = "SuspiciousPhoneTransfer";
    private static final String OP_CREATE = "CREATE";
    private static final String OP_UPDATE = "UPDATE";
    private static final String USER = "user";
    private static final String EDITOR = "editor";
    private static final String CREATOR = "creator";
    private static final String MODIFIER = "modifier";
    private static final String JSON_NEW = "{\"new\":true}";
    private static final String JSON_OLD = "{\"old\":false}";

    private final AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    @Test
    void toDto_shouldMapEntityToDto() {
        Audit audit = new Audit();
        audit.setId(ID_1);
        audit.setEntityType(ENTITY_TYPE_CARD);
        audit.setOperationType(OP_CREATE);
        audit.setCreatedBy(USER);
        audit.setModifiedBy(EDITOR);
        audit.setCreatedAt(LocalDateTime.now());
        audit.setModifiedAt(LocalDateTime.now());
        audit.setNewEntityJson(JSON_NEW);
        audit.setEntityJson(JSON_OLD);
        AuditDto dto = mapper.toDtoAudit(audit);
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(audit.getId());
        assertThat(dto.getEntityType()).isEqualTo(audit.getEntityType());
        assertThat(dto.getOperationType()).isEqualTo(audit.getOperationType());
        assertThat(dto.getCreatedBy()).isEqualTo(audit.getCreatedBy());
        assertThat(dto.getModifiedBy()).isEqualTo(audit.getModifiedBy());
        assertThat(dto.getEntityJson()).isEqualTo(audit.getEntityJson());
        assertThat(dto.getNewEntityJson()).isEqualTo(audit.getNewEntityJson());
    }

    @Test
    void toEntity_shouldIgnoreIdAndMapFields() {
        AuditDto dto = new AuditDto();
        dto.setId(ID_999);
        dto.setEntityType(ENTITY_TYPE_PHONE);
        dto.setOperationType(OP_UPDATE);
        dto.setCreatedBy(CREATOR);
        dto.setModifiedBy(MODIFIER);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setModifiedAt(LocalDateTime.now());
        dto.setNewEntityJson(JSON_NEW);
        dto.setEntityJson(JSON_OLD);
        Audit entity = mapper.toEntityAudit(dto);
        assertThat(entity).isNotNull();
        assertNull(entity.getId());
        assertThat(entity.getEntityType()).isEqualTo(dto.getEntityType());
        assertThat(entity.getOperationType()).isEqualTo(dto.getOperationType());
        assertThat(entity.getCreatedBy()).isEqualTo(dto.getCreatedBy());
        assertThat(entity.getModifiedBy()).isEqualTo(dto.getModifiedBy());
        assertThat(entity.getEntityJson()).isEqualTo(dto.getEntityJson());
        assertThat(entity.getNewEntityJson()).isEqualTo(dto.getNewEntityJson());
    }
}
