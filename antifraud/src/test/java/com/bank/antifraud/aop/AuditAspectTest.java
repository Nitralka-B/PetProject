package com.bank.antifraud.aop;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.enums.OperationType;
import com.bank.antifraud.services.AuditService;
import com.bank.antifraud.util.AuditConstans;
import com.bank.antifraud.util.EntityIdResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuditAspectTest {
    @Mock
    AuditService auditService;
    @Mock
    EntityIdResolver entityIdResolver;
    @Mock
    JoinPoint joinPoint;
    @Mock
    Signature signature;
    private ThreadLocalAuditContext ctx;
    private Clock clock;
    private AuditAspect aspect;

    private static final Instant FIXED_INSTANT = Instant.parse("2025-01-01T00:00:00Z");
    private static final ZoneId ZONE = ZoneId.systemDefault();
    private static final LocalDateTime NOW = LocalDateTime.ofInstant(FIXED_INSTANT, ZONE);

    private static final long ID_CREATE = 10L;
    private static final long ID_UPDATE = 20L;
    private static final long ID_EXIST = 55L;
    private static final long ID_NO_FIRST = 66L;
    private static final long NEW_ACCOUNT_TRANSFER_ID = 777L;
    private static final String CREATE_METHOD = AuditConstans.CREATE_PREFIX + "Transfer";
    private static final String UPDATE_METHOD = "updateTransfer";
    private static final String TEST_SERVICE_NAME  = "Dummy";
    private static final String PREVIOUS_AUDIT_CREATED_BY_USER_X = "userX";
    private static final String AUDIT_FIRST_AUTHOR_NAME = "john";
    private static final String JSON_ACCOUNT_FRAGMENT = "\"accountTransferId\":" + NEW_ACCOUNT_TRANSFER_ID;
    private static final String JSON_FRAGMENT_WITH_ID_EXIST = "\"id\":" + ID_EXIST;
    private static final String JSON_OLD_FRAGMENT = "{\"old\":true}";
    private static final String JSON_FIELD_ACCOUNT_TRANSFER_ID = "\"accountTransferId\"";
    private static final String ENTITY_TYPE_ACCOUNT = "SuspiciousAccountTransfer";

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(FIXED_INSTANT, ZONE);
        ctx = new ThreadLocalAuditContext();
        aspect = new AuditAspect(
                auditService,
                new ObjectMapper(),
                clock,
                ctx,
                entityIdResolver
        );
        lenient().when(joinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    void auditCreate_shouldLogCorrectAudit() {
        when(signature.getName()).thenReturn(CREATE_METHOD);
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setId(ID_CREATE);
        dto.setAccountTransferId(555L);
        when(entityIdResolver.resolve(eq(dto))).thenReturn(ID_CREATE);
        ArgumentCaptor<AuditDto> captor = ArgumentCaptor.forClass(AuditDto.class);
        aspect.auditCreateOrUpdate(joinPoint, dto);
        verify(auditService).logAudit(captor.capture());
        AuditDto logged = captor.getValue();
        assertThat(logged.getId()).isEqualTo(ID_CREATE);
        assertThat(logged.getOperationType()).isEqualTo(OperationType.CREATE.name());
        assertThat(logged.getEntityType()).isEqualTo(ENTITY_TYPE_ACCOUNT);
        assertThat(logged.getEntityJson()).contains(JSON_FIELD_ACCOUNT_TRANSFER_ID);
        assertThat(logged.getNewEntityJson()).isNull();
        assertThat(logged.getCreatedBy()).isEqualTo(AuditConstans.SYSTEM);
        assertThat(logged.getModifiedBy()).isNull();
        assertThat(logged.getCreatedAt()).isEqualTo(NOW);
    }

    @Test
    void auditUpdate_shouldMergeOldAndNewState() {
        AuditDto old = new AuditDto();
        old.setEntityJson(JSON_OLD_FRAGMENT);
        old.setCreatedBy(PREVIOUS_AUDIT_CREATED_BY_USER_X);
        old.setCreatedAt(NOW);
        ctx.setOldAudit(old);
        when(signature.getName()).thenReturn(UPDATE_METHOD);
        SuspiciousAccountTransferDto updated = new SuspiciousAccountTransferDto();
        updated.setId(ID_UPDATE);
        updated.setAccountTransferId(NEW_ACCOUNT_TRANSFER_ID);
        when(entityIdResolver.resolve(eq(updated))).thenReturn(ID_UPDATE);
        ArgumentCaptor<AuditDto> captor = ArgumentCaptor.forClass(AuditDto.class);
        aspect.auditCreateOrUpdate(joinPoint, updated);
        verify(auditService).logAudit(captor.capture());
        AuditDto logged = captor.getValue();
        assertThat(logged.getOperationType()).isEqualTo(OperationType.UPDATE.name());
        assertThat(logged.getEntityJson()).isEqualTo(old.getEntityJson());
        assertThat(logged.getNewEntityJson()).contains(JSON_ACCOUNT_FRAGMENT);
        assertThat(logged.getCreatedBy()).isEqualTo(PREVIOUS_AUDIT_CREATED_BY_USER_X);
        assertThat(logged.getModifiedBy()).isEqualTo(AuditConstans.SYSTEM);
        assertThat(ctx.getOldAudit()).isNull();
    }

    static class DummyServiceImpl {
    }

    @Test
    void captureOldAudit_shouldUseFirstAuditMetadataIfExists() {
        when(joinPoint.getTarget()).thenReturn(new DummyServiceImpl());
        Long id = ID_EXIST;
        SuspiciousAccountTransferDto prevDto = new SuspiciousAccountTransferDto();
        prevDto.setId(id);
        when(auditService.findDtoById(TEST_SERVICE_NAME, id)).thenReturn(prevDto);
        AuditDto first = new AuditDto();
        first.setCreatedBy(AUDIT_FIRST_AUTHOR_NAME);
        first.setCreatedAt(NOW);
        when(auditService.findFirstAudit(TEST_SERVICE_NAME, id)).thenReturn(first);
        aspect.captureOldAudit(joinPoint, java.util.Optional.of(id));
        AuditDto stored = ctx.getOldAudit();
        assertThat(stored).isNotNull();
        assertThat(stored.getEntityJson()).contains(JSON_FRAGMENT_WITH_ID_EXIST);
        assertThat(stored.getCreatedBy()).isEqualTo(AUDIT_FIRST_AUTHOR_NAME);
        assertThat(stored.getCreatedAt()).isEqualTo(NOW);
    }

    @Test
    void captureOldAudit_shouldSetSystemUserWhenNoFirstAudit() {
        when(joinPoint.getTarget()).thenReturn(new DummyServiceImpl());
        Long id = ID_NO_FIRST;
        SuspiciousAccountTransferDto prevDto = new SuspiciousAccountTransferDto();
        prevDto.setId(id);
        when(auditService.findDtoById(TEST_SERVICE_NAME, id)).thenReturn(prevDto);
        when(auditService.findFirstAudit(TEST_SERVICE_NAME, id)).thenReturn(null);
        aspect.captureOldAudit(joinPoint, java.util.Optional.of(id));
        AuditDto stored = ctx.getOldAudit();
        assertThat(stored).isNotNull();
        assertThat(stored.getCreatedBy()).isEqualTo(AuditConstans.SYSTEM);
        assertThat(stored.getCreatedAt()).isEqualTo(NOW);
    }
}