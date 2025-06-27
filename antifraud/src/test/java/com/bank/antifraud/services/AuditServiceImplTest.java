package com.bank.antifraud.services;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.Audit;
import com.bank.antifraud.kafka.AuditProducer;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repositories.AuditRepository;
import com.bank.antifraud.util.AuditConstans;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuditServiceImplTest {
    @Mock
    private SuspiciousTransferService<SuspiciousCardTransferDto> cardService;
    @Mock
    private SuspiciousTransferService<SuspiciousPhoneTransferDto> phoneService;
    @Mock
    private SuspiciousTransferService<SuspiciousAccountTransferDto> accountService;
    @Mock
    private AuditRepository auditRepo;
    @Mock
    private AuditProducer auditProducer;
    @Mock
    private AuditMapper auditMapper;

    @InjectMocks
    private AuditServiceImpl service;

    private static final int SIZE_ONE = 1;
    private static final long ACCOUNT_ID = 30L;
    private static final long PHONE_ID = 20L;
    private static final long CARD_ID = 10L;
    private static final long ONE_L = 1L;
    private static final String ENTITY_ACCOUNT = AuditConstans.ENTITY_TYPE_SUSPICIOUS_ACCOUNT_TRANSFER;
    private static final String ENTITY_PHONE = AuditConstans.ENTITY_TYPE_SUSPICIOUS_PHONE_TRANSFER;
    private static final String ENTITY_CARD = AuditConstans.ENTITY_TYPE_SUSPICIOUS_CARD_TRANSFER;
    private static final String TYPE_FOO = "FOO";
    private static final String TYPE_X = "X";
    private static final long ID_5 = 5L;
    private static final long ID_7 = 7L;

    @Test
    void logAudit() {
        AuditDto auditDto = new AuditDto();
        service.logAudit(auditDto);
        verify(auditProducer).sendAuditEvent(auditDto);
    }

    @Test
    void getAllAuditLogs() {
        Audit e1 = new Audit();
        AuditDto d1 = new AuditDto();
        when(auditRepo.findAll()).thenReturn(List.of(e1));
        when(auditMapper.toDtoAudit(e1)).thenReturn(d1);
        List<AuditDto> result = service.getAllAuditLogs();
        assertEquals(SIZE_ONE, result.size());
        assertSame(d1, result.get(0));
    }

    @Test
    void findDtoById_Account() {
        AuditServiceImpl auditService = new AuditServiceImpl(cardService, phoneService, accountService,
                auditRepo, auditProducer, auditMapper);
        SuspiciousAccountTransferDto expected = new SuspiciousAccountTransferDto();
        expected.setId(ACCOUNT_ID);
        when(accountService.getTransferById(ACCOUNT_ID)).thenReturn(expected);
        Object result = auditService.findDtoById(ENTITY_ACCOUNT, ACCOUNT_ID);
        verify(accountService).getTransferById(ACCOUNT_ID);
        assertNotNull(result);
        assertSame(expected, result);
        assertInstanceOf(SuspiciousAccountTransferDto.class, result);
    }

    @Test
    void findDtoById_Phone() {
        AuditServiceImpl auditService = new AuditServiceImpl(cardService, phoneService, accountService,
                auditRepo, auditProducer, auditMapper);
        SuspiciousPhoneTransferDto expected = new SuspiciousPhoneTransferDto();
        expected.setId(PHONE_ID);
        when(phoneService.getTransferById(PHONE_ID)).thenReturn(expected);
        Object result = auditService.findDtoById(ENTITY_PHONE, PHONE_ID);
        verify(phoneService).getTransferById(PHONE_ID);
        assertNotNull(result);
        assertSame(expected, result);
        assertInstanceOf(SuspiciousPhoneTransferDto.class, result);
    }

    @Test
    void findDtoById_Card() {
        AuditServiceImpl auditService = new AuditServiceImpl(cardService, phoneService, accountService,
                auditRepo, auditProducer, auditMapper);
        SuspiciousCardTransferDto expected = new SuspiciousCardTransferDto();
        expected.setId(CARD_ID);
        when(cardService.getTransferById(CARD_ID)).thenReturn(expected);
        Object result = auditService.findDtoById(ENTITY_CARD, CARD_ID);
        verify(cardService).getTransferById(CARD_ID);
        assertNotNull(result);
        assertSame(expected, result);
        assertInstanceOf(SuspiciousCardTransferDto.class, result);
    }

    @Test
    void findDtoById_Unknown_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> service.findDtoById(TYPE_FOO, ONE_L));
    }

    @Test
    void findLastAudit_MapsAudit() {
        Audit e = new Audit();
        AuditDto d = new AuditDto();
        when(auditRepo.findFirstByEntityTypeAndIdOrderByCreatedAtDesc(TYPE_X, ID_5)).thenReturn(e);
        when(auditMapper.toDtoAudit(e)).thenReturn(d);

        AuditDto result = service.findLastAudit(TYPE_X, ID_5);
        assertSame(d, result);
    }

    @Test
    void findFirstAudit_NullId_ReturnsNull() {
        assertNull(service.findFirstAudit(TYPE_X, null));
    }

    @Test
    void findFirstAudit_MapsAudit() {
        Audit e = new Audit();
        AuditDto d = new AuditDto();
        when(auditRepo
                .findFirstByEntityTypeAndEntityJsonContainingIgnoreCaseOrderByCreatedAtAsc(
                        TYPE_X, AuditConstans.AUDIT_PATTERN + ID_7))
                .thenReturn(e);
        when(auditMapper.toDtoAudit(e)).thenReturn(d);

        AuditDto result = service.findFirstAudit(TYPE_X, ID_7);
        assertSame(d, result);
    }
}