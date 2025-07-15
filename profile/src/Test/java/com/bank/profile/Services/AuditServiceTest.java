package com.bank.profile.Services;

import com.bank.profile.DTO.PrincipalUserDto;
import com.bank.profile.Entities.Audit;
import com.bank.profile.Exceptions.AuditException;
import com.bank.profile.Exceptions.NotAuthorizedException;
import com.bank.profile.Repositories.AuditRepository;
import com.bank.profile.Utils.OperationType;
import com.bank.profile.Utils.TestUtils;
import com.bank.profile.kafka.AuditPrincipalConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.bank.profile.Utils.Constraints.NOT_AUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.bank.profile.Utils.OperationType.CREATE;
import static com.bank.profile.Utils.OperationType.UPDATE;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AuditPrincipalConsumer auditPrincipalConsumer;

    @InjectMocks
    private AuditServiceImpl auditService;

    private static final String TEST_ENTITY_TYPE = "Profile";
    private static final String TEST_USER_NAME = "TestUser";
    private static final String TEST_ENTITY_JSON = "{\"id\":1}";
    private static final Object TEST_ENTITY = new Object();
    private static final Long TEST_ENTITY_ID = 1L;
    private static final Audit TEST_CREATION_AUDIT = new Audit();
    private static final Audit TEST_EXPECTED_AUDIT = new Audit();

    @Test
    void auditCreate_Success() throws JsonProcessingException {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);
        when(objectMapper.writeValueAsString(TEST_ENTITY)).thenReturn(TEST_ENTITY_JSON);

        auditService.auditCreate(TEST_ENTITY_TYPE, CREATE, TEST_ENTITY);

        verify(auditRepository, times(1)).save(any(Audit.class));
    }

    @Test
    void auditCreate_WhenNullParameters_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> auditService.auditCreate(null, null, null));
    }

    @Test
    void auditCreate_WhenNotAuthorized_ShouldThrowException() {
        when(auditPrincipalConsumer.getPrincipal()).thenReturn(null);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> auditService.auditCreate(TEST_ENTITY_TYPE, CREATE, TEST_ENTITY));

        assertEquals(NOT_AUTHORIZED, exception.getMessage());
        verify(auditRepository, never()).save(any());
    }

    @Test
    void auditCreate_WhenPrincipalWithoutUsername_ShouldThrowException() {
        PrincipalUserDto principal = mock(PrincipalUserDto.class);
        when(principal.getUsername()).thenReturn(null);
        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> auditService.auditCreate(TEST_ENTITY_TYPE, CREATE, TEST_ENTITY));

        assertEquals(NOT_AUTHORIZED, exception.getMessage());
        verify(auditRepository, never()).save(any());
    }

    @Test
    void auditCreate_WhenJsonProcessingException_ShouldLogError() throws JsonProcessingException {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);
        when(objectMapper.writeValueAsString(TEST_ENTITY)).thenThrow(JsonProcessingException.class);

        assertDoesNotThrow(() -> auditService.auditCreate(TEST_ENTITY_TYPE, CREATE, TEST_ENTITY));
    }

    @Test
    void auditUpdate_Success() throws JsonProcessingException {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode idNode = mock(JsonNode.class);

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);
        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(idNode);
        when(idNode.asLong()).thenReturn(TEST_ENTITY_ID);
        when(auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                TEST_ENTITY_TYPE, CREATE.name(), TEST_ENTITY_ID))
                .thenReturn(Optional.of(TEST_CREATION_AUDIT));
        when(objectMapper.writeValueAsString(TEST_ENTITY)).thenReturn(TEST_ENTITY_JSON);

        auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY);

        verify(auditRepository, times(1)).save(any(Audit.class));
    }

    @Test
    void auditUpdate_WhenNullParameters_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> auditService.auditUpdate(null, null, null));
    }

    @Test
    void auditUpdate_WhenNotAuthorized_ShouldThrowException() {
        when(auditPrincipalConsumer.getPrincipal()).thenReturn(null);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));

        assertEquals(NOT_AUTHORIZED, exception.getMessage());
        verify(auditRepository, never()).save(any());
    }

    @Test
    void auditUpdate_WhenPrincipalWithoutUsername_ShouldThrowException() {
        PrincipalUserDto principal = mock(PrincipalUserDto.class);
        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);

        NotAuthorizedException exception = assertThrows(NotAuthorizedException.class,
                () -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));

        assertEquals(NOT_AUTHORIZED, exception.getMessage());
        verify(auditRepository, never()).save(any());

        verify(objectMapper, never()).valueToTree(any());
    }

    @Test
    void auditUpdate_WhenCannotExtractId_ShouldThrowException() {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);

        when(objectMapper.valueToTree(TEST_ENTITY)).thenThrow(new RuntimeException("JSON processing error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));

        assertEquals("Failed to extract ID from entity", exception.getMessage());
        verify(auditRepository, never()).save(any());
    }

    @Test
    void auditUpdate_WhenCreationAuditNotFound_ShouldThrowException() throws JsonProcessingException {
        PrincipalUserDto principalMock = mock(PrincipalUserDto.class);
        when(principalMock.getUsername()).thenReturn(TEST_USER_NAME);

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principalMock);

        JsonNode jsonNodeMock = mock(JsonNode.class);
        JsonNode idNodeMock = mock(JsonNode.class);
        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNodeMock);
        when(jsonNodeMock.get("id")).thenReturn(idNodeMock);
        when(idNodeMock.asLong()).thenReturn(TEST_ENTITY_ID);

        when(auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                TEST_ENTITY_TYPE, CREATE.name(), TEST_ENTITY_ID))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));
    }

    @Test
    void auditUpdate_WhenJsonProcessingException_ShouldLogError() throws JsonProcessingException {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode idNode = mock(JsonNode.class);

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);
        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(idNode);
        when(idNode.asLong()).thenReturn(TEST_ENTITY_ID);
        when(auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                TEST_ENTITY_TYPE, CREATE.name(), TEST_ENTITY_ID))
                .thenReturn(Optional.of(TEST_CREATION_AUDIT));
        when(objectMapper.writeValueAsString(TEST_ENTITY)).thenThrow(JsonProcessingException.class);

        assertDoesNotThrow(() -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));

        verify(auditRepository, never()).save(any());
    }

    @Test
    void auditUpdate_WhenAuditException_ShouldLogError() throws JsonProcessingException {
        PrincipalUserDto principal = TestUtils.getPrincipal();

        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode idNode = mock(JsonNode.class);

        when(auditPrincipalConsumer.getPrincipal()).thenReturn(principal);
        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(idNode);
        when(idNode.asLong()).thenReturn(TEST_ENTITY_ID);
        when(auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                TEST_ENTITY_TYPE, CREATE.name(), TEST_ENTITY_ID))
                .thenThrow(new AuditException("Test exception"));

        assertDoesNotThrow(() -> auditService.auditUpdate(TEST_ENTITY_TYPE, UPDATE, TEST_ENTITY));

        verify(auditRepository, never()).save(any());
    }

    @Test
    void findCreationAudit_WhenAuditExists_ShouldReturnAudit() {
        when(auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                TEST_ENTITY_TYPE, CREATE.name(), TEST_ENTITY_ID))
                .thenReturn(Optional.of(TEST_EXPECTED_AUDIT));

        Audit result = auditService.findCreationAudit(TEST_ENTITY_TYPE, TEST_ENTITY_ID);

        assertEquals(TEST_EXPECTED_AUDIT, result);
    }

    @Test
    void extractIdFromEntity_WhenIdExists_ShouldReturnId() {
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode idNode = mock(JsonNode.class);

        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(idNode);
        when(idNode.isNull()).thenReturn(false);
        when(idNode.asLong()).thenReturn(1L);

        Long result = auditService.extractIdFromEntity(TEST_ENTITY);

        assertEquals(1L, result);
    }

    @Test
    void extractIdFromEntity_WhenIdIsNull_ShouldThrowException() {
        JsonNode jsonNode = mock(JsonNode.class);

        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> auditService.extractIdFromEntity(TEST_ENTITY));

        assertEquals("Failed to extract ID from entity", exception.getMessage());
    }

    @Test
    void extractIdFromEntity_WhenIdNodeIsNull_ShouldThrowException() {
        JsonNode jsonNode = mock(JsonNode.class);
        JsonNode idNode = mock(JsonNode.class);

        when(objectMapper.valueToTree(TEST_ENTITY)).thenReturn(jsonNode);
        when(jsonNode.get("id")).thenReturn(idNode);
        when(idNode.isNull()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> auditService.extractIdFromEntity(TEST_ENTITY));

        assertEquals("Failed to extract ID from entity", exception.getMessage());
    }
}
