package com.bank.antifraud.services;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entities.SuspiciousAccountTransfer;
import com.bank.antifraud.kafka.SuspiciousTransferProducer;
import com.bank.antifraud.mapper.AccountTransferMapper;
import com.bank.antifraud.repositories.SuspiciousAccountTransferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import com.bank.antifraud.TestConstants;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SuspiciousAccountTransferServiceImplTest {

    @Mock
    private SuspiciousAccountTransferRepository repository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AccountTransferMapper mapper;
    @Mock
    private SuspiciousTransferProducer producer;

    @InjectMocks
    private SuspiciousAccountTransferServiceImpl service;

    private SuspiciousAccountTransfer entity;
    private SuspiciousAccountTransferDto dto;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        dto = new SuspiciousAccountTransferDto();
        dto.setId(TestConstants.ID_1);
        dto.setAccountTransferId(TestConstants.ID_1);
        dto.setIsBlocked(TestConstants.BOOL_TRUE);
        dto.setIsSuspicious(TestConstants.BOOL_TRUE);
        dto.setBlockedReason(TestConstants.REASON_TOO_LARGE);
        dto.setSuspiciousReason(TestConstants.REASON_SUM_TOO_LARGE);
        entity = new SuspiciousAccountTransfer();
        entity.setId(TestConstants.ID_1);
        entity.setAccountTransferId(TestConstants.ID_1);
        entity.setIsBlocked(TestConstants.BOOL_TRUE);
        entity.setIsSuspicious(TestConstants.BOOL_TRUE);
        entity.setBlockedReason(TestConstants.REASON_TOO_LARGE);
        entity.setSuspiciousReason(TestConstants.REASON_SUM_TOO_LARGE);
        when(mapper.toEntity(any(SuspiciousAccountTransferDto.class))).thenReturn(entity);
        when(mapper.toDto(any(SuspiciousAccountTransfer.class))).thenReturn(dto);
        doNothing().when(mapper).updateFromDto(any(SuspiciousAccountTransferDto.class),
                any(SuspiciousAccountTransfer.class));
        when(repository.save(any(SuspiciousAccountTransfer.class))).thenReturn(entity);
        when(repository.findById(TestConstants.ID_1)).thenReturn(Optional.of(entity));
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        when(repository.existsByAccountTransferId(TestConstants.ID_1)).thenReturn(TestConstants.BOOL_TRUE);
        when(repository.findByAccountTransferId(TestConstants.ID_1)).thenReturn(Optional.of(entity));
        doReturn("{}")
                .when(objectMapper)
                .writeValueAsString(any());
        doNothing().when(producer).sendCreate(any());
        doNothing().when(producer).sendUpdate(any());
        doNothing().when(producer).sendDelete(any());
        doNothing().when(producer).sendGet(any());
    }

    @Test
    void existsByTransferId_WhenExists_ReturnsTrue() {
        Long transferId = TestConstants.ID_1;
        when(repository.existsByAccountTransferId(transferId)).thenReturn(TestConstants.BOOL_TRUE);
        boolean result = service.existsByTransferId(transferId);
        assertTrue(result);
        verify(repository).existsByAccountTransferId(eq(transferId));
    }

    @Test
    void existsByTransferId_WhenNotExists_ReturnsFalse() {
        Long transferId = TestConstants.ID_999;
        when(repository.existsByAccountTransferId(transferId)).thenReturn(TestConstants.BOOL_FALSE);
        boolean result = service.existsByTransferId(transferId);
        assertFalse(result);
        verify(repository).existsByAccountTransferId(eq(transferId));
    }

    @Test
    void findIdByTransferId_WhenExists_ReturnsId() {
        // given
        Long transferId = TestConstants.ID_1;
        Long expectedId = TestConstants.ID_77;
        SuspiciousAccountTransfer transfer = new SuspiciousAccountTransfer();
        transfer.setId(expectedId);
        when(repository.findByAccountTransferId(transferId)).thenReturn(Optional.of(transfer));
        Optional<Long> result = service.findIdByTransferId(transferId);
        assertTrue(result.isPresent());
        assertEquals(expectedId, result.get());
        verify(repository).findByAccountTransferId(eq(transferId));
    }

    @Test
    void findIdByTransferId_WhenNotExists_ReturnsEmpty() {
        Long transferId = TestConstants.ID_999;
        when(repository.findByAccountTransferId(transferId)).thenReturn(Optional.empty());
        Optional<Long> result = service.findIdByTransferId(transferId);
        assertTrue(result.isEmpty());
        verify(repository).findByAccountTransferId(eq(transferId));
    }

    @Test
    void createTransfer_ValidDto_ReturnsCreatedDto() throws JsonProcessingException {
        // given
        when(repository.save(any(SuspiciousAccountTransfer.class))).thenReturn(entity);
        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        SuspiciousAccountTransferDto result = service.createTransfer(dto);
        assertNotNull(result);
        assertEquals(dto, result);

        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
        verify(producer).sendCreate(kafkaMessageCaptor.capture());

        String kafkaMessage = kafkaMessageCaptor.getValue();
        assertNotNull(kafkaMessage);
    }

    @Test
    void getTransferById_WhenExists_ReturnsDto() {
        Long id = TestConstants.ID_1;
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        SuspiciousAccountTransferDto result = service.getTransferById(id);
        assertNotNull(result);
        assertEquals(dto, result);
        verify(repository).findById(id);
        verify(mapper).toDto(entity);
    }

    @Test
    void getTransferById_WhenNotExists_ThrowsException() {
        Long nonExistentId = TestConstants.ID_99;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getTransferById(nonExistentId));
        verify(repository).findById(nonExistentId);
    }

    @Test
    void getAllTransfers_ReturnsListOfDtos() throws JsonProcessingException {
        List<SuspiciousAccountTransfer> entities = Collections.singletonList(entity);
        when(repository.findAll()).thenReturn(entities);
        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        List<SuspiciousAccountTransferDto> result = service.getAllTransfers();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(TestConstants.SIZE_ONE, result.size());
        assertEquals(dto, result.get(0));
        verify(repository).findAll();
        verify(mapper).toDto(entity);
        verify(producer).sendGet(kafkaMessageCaptor.capture());
        String kafkaMessage = kafkaMessageCaptor.getValue();
        assertNotNull(kafkaMessage);
    }

    @Test
    void updateTransfer_ValidIdAndDto_ReturnsUpdatedDto() throws JsonProcessingException {
        Long id = TestConstants.ID_1;
        SuspiciousAccountTransferDto updatedDto = new SuspiciousAccountTransferDto();
        updatedDto.setAccountTransferId(TestConstants.ID_1);
        updatedDto.setIsBlocked(TestConstants.BOOL_FALSE);
        updatedDto.setIsSuspicious(TestConstants.BOOL_FALSE);
        updatedDto.setBlockedReason(TestConstants.REASON_NEW);
        updatedDto.setSuspiciousReason(TestConstants.SUSPICIOUS_REASON_NEW);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any(SuspiciousAccountTransfer.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(updatedDto);
        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        SuspiciousAccountTransferDto result = service.updateTransfer(Optional.of(id), updatedDto);
        assertNotNull(result);
        assertEquals(updatedDto, result);
        verify(repository).findById(id);
        verify(mapper).updateFromDto(eq(updatedDto), eq(entity));
        verify(repository).save(entity);
        verify(mapper).toDto(entity);
        verify(producer).sendUpdate(kafkaMessageCaptor.capture());
        String kafkaMessage = kafkaMessageCaptor.getValue();
        assertNotNull(kafkaMessage);
    }

    @Test
    void updateTransfer_EmptyId_ThrowsException() {
        SuspiciousAccountTransferDto updatedDto = new SuspiciousAccountTransferDto();
        assertThrows(IllegalArgumentException.class,
                () -> service.updateTransfer(Optional.empty(), updatedDto));
        verify(repository, never()).findById(any());
        verify(repository, never()).save(any());
        verify(producer, never()).sendUpdate(anyString());
    }

    @Test
    void updateTransfer_NonExistentId_ThrowsException() {
        Long nonExistentId = TestConstants.ID_999;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.updateTransfer(Optional.of(nonExistentId), dto));
        verify(repository).findById(nonExistentId);
        verify(repository, never()).save(any());
        verify(producer, never()).sendUpdate(anyString());
    }

    @Test
    void deleteTransfer_ValidId_DeletesEntity() throws JsonProcessingException {
        Long id = TestConstants.ID_1;
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        ArgumentCaptor<String> kafkaMessageCaptor = ArgumentCaptor.forClass(String.class);
        service.deleteTransfer(id);
        verify(repository).findById(id);
        verify(repository).delete(entity);
        verify(producer).sendDelete(kafkaMessageCaptor.capture());
        String kafkaMessage = kafkaMessageCaptor.getValue();
        assertNotNull(kafkaMessage);
    }

    @Test
    void deleteTransfer_NonExistentId_ThrowsException() {
        Long nonExistentId = TestConstants.ID_999;
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> service.deleteTransfer(nonExistentId));
        verify(repository).findById(nonExistentId);
        verify(repository, never()).delete(any());
        verify(producer, never()).sendDelete(anyString());
    }
}
