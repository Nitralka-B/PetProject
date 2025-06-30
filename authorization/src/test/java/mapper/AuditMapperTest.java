package mapper;


import com.bank.authorization.dto.AuditDto;

import static com.bank.authorization.entity.OperationType.UPDATE;
import static com.bank.authorization.entity.Role.ADMIN;
import static com.bank.authorization.entity.Role.USER;

import com.bank.authorization.entity.Audit;
import com.bank.authorization.entity.OperationType;
import com.bank.authorization.mapper.AuditMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;


import static com.bank.authorization.entity.OperationType.CREATE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuditMapperTest {

    private AuditMapper mapper;
    private OffsetDateTime dateTime;
    private static final Long USER_ID = 1L;
    private static final Long AUDIT_ID = 1L;
    private static final String TEST_ENTITY_TYPE = "TEST_ENTITY";
    private static final OperationType TEST_OPERATION_TYPE = UPDATE;
    private static final String TEST_CREATED_BY = "TEST_USER";
    private static final OffsetDateTime TEST_CREATED_AT = OffsetDateTime.now();
    private static final OffsetDateTime TEST_MODIFIED_AT = TEST_CREATED_AT.plusHours(2);
    private static final String TEST_ENTITY_JSON = "{\"test\": \"value\"}";
    private static final String TEST_NEW_ENTITY_JSON = "{\"test\": \"value\", \"new\": \"newValue\"}";



    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AuditMapper.class);
        dateTime = OffsetDateTime.now();
    }

    @Test
    @DisplayName("Маппинг из AuditDto в Audit - все поля")
    void toEntityShouldMapAllFields() {
        AuditDto dto = AuditDto.builder()
                .id(USER_ID)
                .entityType(String.valueOf(USER))
                .operationType(CREATE)
                .createdBy(String.valueOf(ADMIN))
                .createdAt(dateTime)
                .modifiedAt(dateTime.plusMinutes(1))
                .entityJson("{\"id\": 1}")
                .newEntityJson("{\"id\": 1, \"name\": \"John\"}")
                .build();

        Audit entity = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getId(), entity.getId()),
                () -> assertEquals(dto.getEntityType(), entity.getEntityType()),
                () -> assertEquals(dto.getOperationType(), entity.getOperationType()),
                () -> assertEquals(dto.getCreatedBy(), entity.getCreatedBy()),
                () -> assertEquals(dto.getCreatedAt(), entity.getCreatedAt()),
                () -> assertEquals(dto.getModifiedAt(), entity.getModifiedAt()),
                () -> assertEquals(dto.getEntityJson(), entity.getEntityJson()),
                () -> assertEquals(dto.getNewEntityJson(), entity.getNewEntityJson())
        );
    }

    @Test
    @DisplayName("Маппинг из Audit в AuditDto - все поля")
    void toDtoShouldMapAllFields() {
        Audit entity = new Audit();
        entity.setId(USER_ID);
        entity.setEntityType(TEST_ENTITY_TYPE);
        entity.setOperationType(TEST_OPERATION_TYPE);
        entity.setCreatedBy(TEST_CREATED_BY);
        entity.setCreatedAt(TEST_CREATED_AT);
        entity.setModifiedAt(TEST_MODIFIED_AT);
        entity.setEntityJson(TEST_ENTITY_JSON);
        entity.setNewEntityJson(TEST_NEW_ENTITY_JSON);

        AuditDto dto = mapper.toDto(entity);

        assertAll(
                () -> assertEquals(AUDIT_ID, dto.getId()),
                () -> assertEquals(TEST_ENTITY_TYPE, dto.getEntityType()),
                () -> assertEquals(TEST_OPERATION_TYPE, dto.getOperationType()),
                () -> assertEquals(TEST_CREATED_BY, dto.getCreatedBy()),
                () -> assertEquals(TEST_CREATED_AT, dto.getCreatedAt()),
                () -> assertEquals(TEST_MODIFIED_AT, dto.getModifiedAt()),
                () -> assertEquals(TEST_ENTITY_JSON, dto.getEntityJson()),
                () -> assertEquals(TEST_NEW_ENTITY_JSON, dto.getNewEntityJson())
        );
    }

}
