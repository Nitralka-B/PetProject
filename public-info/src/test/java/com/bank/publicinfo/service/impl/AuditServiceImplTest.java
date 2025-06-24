package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для проверки функциональности {@link AuditServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private AuditMapper auditMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AuditServiceImpl auditService;

    private AuditDto auditDto;
    private Audit auditEntity;
    private TestAuditableEntity testEntity;

    /**
     * Тестовая реализация интерфейса AuditableEntity.
     */
    private static class TestAuditableEntity implements AuditableEntity {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    /**
     * Инициализация тестовых данных перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        auditDto = TestUtil.createTestAuditDto();
        auditEntity = TestUtil.createTestAuditEntity();
        testEntity = new TestAuditableEntity();
        testEntity.setId(TestConstants.TEST_ID);
    }

    /**
     * Тест успешного логирования аудита.
     */
    @Test
    void log_Success() {
        when(auditMapper.toEntity(auditDto)).thenReturn(auditEntity);
        when(auditRepository.save(auditEntity)).thenReturn(auditEntity);

        assertDoesNotThrow(() -> auditService.log(auditDto),
                TestConstants.OPERATION_SHOULD_NOT_THROW);

        verify(auditMapper).toEntity(auditDto);
        verify(auditRepository).save(auditEntity);
    }

    /**
     * Тест случая, когда аудит запись не найдена.
     */
    @Test
    void getById_NotFound() {
        when(auditRepository.findById(TestConstants.TEST_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> auditService.getById(TestConstants.TEST_ID),
                TestConstants.SHOULD_THROW_EXCEPTION
        );

        assertEquals(
                String.format(TestConstants.ENTITY_NOT_FOUND_WITH_ID_FORMAT,
                        TestConstants.AUDIT_ENTITY_TYPE,
                        TestConstants.TEST_ID,
                        TestConstants.TEST_ID) ,
                exception.getMessage(),
                TestConstants.EXCEPTION_MESSAGE_SHOULD_MATCH
        );
        verify(auditRepository).findById(TestConstants.TEST_ID);
    }

    /**
     * Тест успешного получения состояния сущности.
     */
    @Test
    void getLastEntityState_Success() {
        when(entityManager.find(TestAuditableEntity.class, TestConstants.TEST_ID)).thenReturn(testEntity);

        TestAuditableEntity result = auditService.getLastEntityState(
                TestConstants.TEST_ID, TestAuditableEntity.class);

        assertNotNull(result,
                String.format(TestConstants.SHOULD_NOT_BE_NULL_FORMAT, "Test entity"));
        assertEquals(TestConstants.TEST_ID, result.getId(),
                String.format(TestConstants.SHOULD_MATCH_FORMAT, "Entity ID"));
    }

    /**
     * Тест случая, когда состояние сущности не найдено.
     */
    @Test
    void getLastEntityState_NotFound() {
        when(entityManager.find(TestAuditableEntity.class, TestConstants.TEST_ID)).thenReturn(null);

        TestAuditableEntity result = auditService.getLastEntityState(
                TestConstants.TEST_ID, TestAuditableEntity.class);

        assertNull(result, TestConstants.ENTITY_SHOULD_BE_NULL);
        verify(entityManager).find(TestAuditableEntity.class, TestConstants.TEST_ID);
    }

    /**
     * Тест обработки исключения при получении состояния сущности.
     */
    @Test
    void getLastEntityState_Exception() {
        when(entityManager.find(TestAuditableEntity.class, TestConstants.TEST_ID))
                .thenThrow(new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE));

        TestAuditableEntity result = auditService.getLastEntityState(
                TestConstants.TEST_ID, TestAuditableEntity.class);

        assertNull(result, TestConstants.ENTITY_SHOULD_BE_NULL);
        verify(entityManager).find(TestAuditableEntity.class, TestConstants.TEST_ID);
    }

    /**
     * Тест успешного получения аудит записи по ID.
     */
    @Test
    void getById_Success() {
        when(auditRepository.findById(TestConstants.TEST_ID)).thenReturn(Optional.of(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);

        AuditDto result = auditService.getById(TestConstants.TEST_ID);

        assertNotNull(result, TestConstants.DTO_SHOULD_NOT_BE_NULL);
        assertEquals(auditDto, result, TestConstants.DTO_SHOULD_MATCH_EXPECTED);
        verify(auditRepository).findById(TestConstants.TEST_ID);
        verify(auditMapper).toDto(auditEntity);
    }

    /**
     * Тест обработки ошибки при логировании.
     */
    @Test
    void log_Failure() {
        when(auditMapper.toEntity(auditDto)).thenReturn(auditEntity);
        when(auditRepository.save(auditEntity))
                .thenThrow(new RuntimeException(TestConstants.DB_ERROR_MESSAGE));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> auditService.log(auditDto),
                TestConstants.SHOULD_THROW_EXCEPTION
        );

        assertEquals(
                String.format(TestConstants.ENTITY_SAVE_FAILED_FORMAT,
                        TestConstants.AUDIT_ENTITY_TYPE.toLowerCase()),
                exception.getMessage(),
                TestConstants.EXCEPTION_MESSAGE_SHOULD_MATCH
        );
        verify(auditMapper).toEntity(auditDto);
        verify(auditRepository).save(auditEntity);
    }

    /**
     * Тест успешного получения всех аудит записей.
     */
    @Test
    void getAll_Success() {
        List<Audit> audits = List.of(auditEntity);
        List<AuditDto> auditDtos = List.of(auditDto);

        when(auditRepository.findAll()).thenReturn(audits);
        when(auditMapper.toDtoList(audits)).thenReturn(auditDtos);

        List<AuditDto> result = auditService.getAll();

        assertNotNull(result, TestConstants.RESULT_LIST_SHOULD_NOT_BE_NULL);
        assertEquals(TestConstants.SINGLE_ELEMENT_LIST_SIZE, result.size(),
                TestConstants.LIST_SIZE_SHOULD_MATCH);
        assertEquals(auditDtos, result, TestConstants.RESULT_LIST_SHOULD_MATCH_EXPECTED);
        verify(auditRepository).findAll();
        verify(auditMapper).toDtoList(audits);
    }

    /**
     * Тест успешного получения аудит записи через метод getAuditRecordById.
     */
    @Test
    void getAuditRecordById_Success() {
        when(auditRepository.findById(TestConstants.TEST_ID)).thenReturn(Optional.of(auditEntity));
        when(auditMapper.toDto(auditEntity)).thenReturn(auditDto);

        AuditDto result = auditService.getAuditRecordById(TestConstants.TEST_ID);

        assertNotNull(result, TestConstants.DTO_SHOULD_NOT_BE_NULL);
        assertEquals(auditDto, result, TestConstants.DTO_SHOULD_MATCH_EXPECTED);
        verify(auditRepository).findById(TestConstants.TEST_ID);
        verify(auditMapper).toDto(auditEntity);
    }
}
