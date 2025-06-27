package com.bank.antifraud.kafka;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.dto.SuspiciousFactoryDto;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.services.SuspiciousAccountTransferServiceImpl;
import com.bank.antifraud.services.SuspiciousCardTransferServiceImpl;
import com.bank.antifraud.services.SuspiciousPhoneTransferServiceImpl;
import com.bank.antifraud.util.KafkaConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TransferConsumerTest {
    @Mock
    private SuspiciousAccountTransferServiceImpl accountService;

    @Mock
    private SuspiciousCardTransferServiceImpl cardService;

    @Mock
    private SuspiciousPhoneTransferServiceImpl phoneService;

    @Mock
    private TransferProducer producer;

    @Mock
    private SuspiciousFactoryDto factory;

    private TransferConsumer consumer;

    private static final String ACCOUNT = "ACCOUNT";
    private static final String CARD = "CARD";
    private static final String PHONE = "PHONE";
    private static final String UNKNOWN = "unknown";
    private static final long ID_1 = 1L;
    private static final long ID_123 = 123L;
    private static final String LIMIT = "100000";
    private static final BigDecimal BD_LIMIT = new BigDecimal(LIMIT);
    private static final BigDecimal BD_150000 = new BigDecimal("150000");
    private static final BigDecimal BD_100000 = new BigDecimal("100000");
    private static final BigDecimal BD_50000 = new BigDecimal("50000");

    private static final String PURPOSE_TEST = "testPurpose";
    private static final String PURPOSE_CARD_TEST = "cardTest";
    private static final String PURPOSE_PHONE_TEST = "phoneTest";
    private static final String PURPOSE_BLOCKED_CREATE = "blockedCreate";
    private static final String PURPOSE_BLOCKED_UPDATE = "blockedUpdate";
    private static final String PURPOSE_CLEAN_CREATE = "cleanCreate";
    private static final String PURPOSE_CLEAN_UPDATE = "cleanUpdate";
    private static final String PURPOSE_GENERIC = "purpose";

    private static final String METHOD_APPLY_DTO_TO_SERVICE = "applyDtoToService";
    private static final String METHOD_PROCESS_TRANSFER = "processTransfer";
    private static final String METHOD_GET_ENTITY_ID_BY_TRANSFER_ID = "getEntityIdByTransferId";
    private static final String METHOD_IS_BLOCKED = "isBlocked";
    private static final String METHOD_LIMIT = "limit";

    private static final Class<?>[] PARAMS_APPLY_DTO_TO_SERVICE = {String.class, Object.class, Optional.class};
    private static final Class<?>[] PARAMS_PROCESS_TRANSFER = {TransferChecked.class, String.class,
            TransferConsumer.ExistsChecker.class};
    private static final Class<?>[] PARAMS_GET_ENTITY_ID_BY_TRANSFER_ID = {String.class, Long.class};
    private static final Class<?>[] PARAMS_IS_BLOCKED = {BigDecimal.class};

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        consumer = new TransferConsumer(accountService, cardService, phoneService, factory, producer);
        Field field = TransferConsumer.class.getDeclaredField(METHOD_LIMIT);
        field.setAccessible(true);
        field.set(consumer, BD_LIMIT);
    }

    private Object invokePrivate(Object target, String methodName, Class<?>[] paramTypes,
                                 Object... args) throws Exception {
        Method m = target.getClass().getDeclaredMethod(methodName, paramTypes);
        m.setAccessible(true);
        return m.invoke(target, args);
    }

    @Test
    void testApplyDtoToService_accountCreate() throws Exception {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                ACCOUNT, dto, Optional.empty());
        verify(accountService).createTransfer(dto);
        verify(accountService, never()).updateTransfer(any(), any());
    }

    @Test
    void testApplyDtoToService_cardCreate() throws Exception {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                CARD, dto, Optional.empty());
        verify(cardService).createTransfer(dto);
        verify(cardService, never()).updateTransfer(any(), any());
    }

    @Test
    void testApplyDtoToService_phoneCreate() throws Exception {
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                PHONE, dto, Optional.empty());
        verify(phoneService).createTransfer(dto);
        verify(phoneService, never()).updateTransfer(any(), any());
    }

    @Test
    void testApplyDtoToService_accountUpdate() throws Exception {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        Long id = ID_1;
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                ACCOUNT, dto, Optional.of(id));
        verify(accountService).updateTransfer(Optional.of(id), dto);
        verify(accountService, never()).createTransfer(any());
    }

    @Test
    void testApplyDtoToService_cardUpdate() throws Exception {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        Long id = ID_1;
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                CARD, dto, Optional.of(id));
        verify(cardService).updateTransfer(Optional.of(id), dto);
        verify(cardService, never()).createTransfer(any());
    }

    @Test
    void testApplyDtoToService_phoneUpdate() throws Exception {
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        Long id = ID_1;
        invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                PHONE, dto, Optional.of(id));
        verify(phoneService).updateTransfer(Optional.of(id), dto);
        verify(phoneService, never()).createTransfer(any());
    }

    @Test
    void testApplyDtoToService_unknownType() {
        Object dto = new Object();
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () ->
                invokePrivate(consumer, METHOD_APPLY_DTO_TO_SERVICE, PARAMS_APPLY_DTO_TO_SERVICE,
                        UNKNOWN, dto, Optional.empty())
        );
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains(UNKNOWN));
    }

    @ParameterizedTest
    @CsvSource({
            "100001, true",
            "70000, false"
    })
    void isBlocked_parametrized(BigDecimal amount, boolean expected) throws Exception {
        boolean actual = (boolean) invokePrivate(
                consumer,
                METHOD_IS_BLOCKED,
                PARAMS_IS_BLOCKED,
                amount
        );
        assertEquals(expected, actual);
    }


    @Test
    void getEntityIdByTransferId_byAccount() throws Exception {
        when(accountService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        Optional<Long> id = (Optional<Long>) invokePrivate(consumer, METHOD_GET_ENTITY_ID_BY_TRANSFER_ID,
                PARAMS_GET_ENTITY_ID_BY_TRANSFER_ID, ACCOUNT, ID_1);
        assertTrue(id.isPresent());
        assertEquals(ID_123, id.get());
        verify(accountService).findIdByTransferId(ID_1);
        verify(cardService, never()).findIdByTransferId(notNull());
        verify(phoneService, never()).findIdByTransferId(notNull());
    }

    @Test
    void getEntityIdByTransferId_byCard() throws Exception {
        when(cardService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        Optional<Long> id = (Optional<Long>) invokePrivate(consumer, METHOD_GET_ENTITY_ID_BY_TRANSFER_ID,
                PARAMS_GET_ENTITY_ID_BY_TRANSFER_ID, CARD, ID_1);
        assertTrue(id.isPresent());
        assertEquals(ID_123, id.get());
        verify(cardService).findIdByTransferId(ID_1);
        verify(accountService, never()).findIdByTransferId(notNull());
        verify(phoneService, never()).findIdByTransferId(notNull());
    }

    @Test
    void getEntityIdByTransferId_byPhone() throws Exception {
        when(phoneService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        Optional<Long> id = (Optional<Long>) invokePrivate(consumer, METHOD_GET_ENTITY_ID_BY_TRANSFER_ID,
                PARAMS_GET_ENTITY_ID_BY_TRANSFER_ID, PHONE, ID_1);
        assertTrue(id.isPresent());
        assertEquals(ID_123, id.get());
        verify(phoneService).findIdByTransferId(ID_1);
        verify(accountService, never()).findIdByTransferId(notNull());
        verify(cardService, never()).findIdByTransferId(notNull());
    }

    @Test
    void getEntityIdByTransferId_unknownType() throws Exception {
        Long transferId = ID_1;
        Optional<Long> result = (Optional<Long>) invokePrivate(consumer, METHOD_GET_ENTITY_ID_BY_TRANSFER_ID,
                PARAMS_GET_ENTITY_ID_BY_TRANSFER_ID, UNKNOWN, transferId);
        assertFalse(result.isPresent());
    }

    @Test
    void listenAccount_shouldProcessTransfer() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_TEST);
        when(transfer.getAmount()).thenReturn(BD_150000);
        when(accountService.existsByTransferId(ID_1)).thenReturn(false);
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        when(factory.createDto(transfer, ACCOUNT)).thenReturn(dto);
        consumer.listenAccount(transfer);
        verify(accountService).existsByTransferId(ID_1);
        verify(factory).createDto(transfer, ACCOUNT);
        verify(accountService).createTransfer(dto);
        verify(producer).sendVerdict(ID_1, ACCOUNT, KafkaConstants.BLOCKED, PURPOSE_TEST);
    }

    @Test
    void listenCard_shouldProcessTransfer() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_CARD_TEST);
        when(transfer.getAmount()).thenReturn(BD_50000);
        when(cardService.existsByTransferId(ID_1)).thenReturn(true);
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        when(factory.updateDto(transfer, CARD)).thenReturn(dto);
        when(cardService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        consumer.listenCard(transfer);
        verify(cardService).existsByTransferId(ID_1);
        verify(factory).updateDto(transfer, CARD);
        verify(cardService).updateTransfer(Optional.of(ID_123), dto);
        verify(producer).sendVerdict(ID_1, CARD, KafkaConstants.ALLOWED, PURPOSE_CARD_TEST);
    }

    @Test
    void listenPhone_shouldProcessTransfer() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_PHONE_TEST);
        when(transfer.getAmount()).thenReturn(BD_100000);
        when(phoneService.existsByTransferId(ID_1)).thenReturn(false);
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        when(factory.createCleanDto(transfer, PHONE)).thenReturn(dto);
        consumer.listenPhone(transfer);
        verify(phoneService).existsByTransferId(ID_1);
        verify(factory).createCleanDto(transfer, PHONE);
        verify(phoneService).createTransfer(dto);
        verify(producer).sendVerdict(ID_1, PHONE, KafkaConstants.ALLOWED, PURPOSE_PHONE_TEST);
    }

    @Test
    void processTransfer_shouldLogWarning_whenNullFields() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getAmount()).thenReturn(null);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_GENERIC);
        invokePrivate(consumer, METHOD_PROCESS_TRANSFER, PARAMS_PROCESS_TRANSFER, transfer, ACCOUNT,
                (TransferConsumer.ExistsChecker) accountService::existsByTransferId);
        verifyNoInteractions(producer);
        verifyNoInteractions(accountService);
    }

    @Test
    void processTransfer_shouldCreate_whenAmountBlockedAndNotExists() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_BLOCKED_CREATE);
        when(transfer.getAmount()).thenReturn(BD_150000);
        when(accountService.existsByTransferId(ID_1)).thenReturn(false);
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        when(factory.createDto(transfer, ACCOUNT)).thenReturn(dto);
        invokePrivate(consumer, METHOD_PROCESS_TRANSFER, PARAMS_PROCESS_TRANSFER, transfer, ACCOUNT,
                (TransferConsumer.ExistsChecker) accountService::existsByTransferId);
        verify(accountService).createTransfer(dto);
        verify(producer).sendVerdict(ID_1, ACCOUNT, KafkaConstants.BLOCKED, PURPOSE_BLOCKED_CREATE);
    }

    @Test
    void processTransfer_shouldUpdate_whenAmountBlockedAndExists() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_BLOCKED_UPDATE);
        when(transfer.getAmount()).thenReturn(BD_150000);
        when(accountService.existsByTransferId(ID_1)).thenReturn(true);
        when(accountService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        when(factory.createDto(transfer, ACCOUNT)).thenReturn(dto);
        invokePrivate(consumer, METHOD_PROCESS_TRANSFER, PARAMS_PROCESS_TRANSFER, transfer, ACCOUNT,
                (TransferConsumer.ExistsChecker) accountService::existsByTransferId);
        verify(accountService).updateTransfer(Optional.of(ID_123), dto);
        verify(producer).sendVerdict(ID_1, ACCOUNT, KafkaConstants.BLOCKED, PURPOSE_BLOCKED_UPDATE);
    }

    @Test
    void processTransfer_shouldCleanCreate_whenAmountNotBlockedAndNotExists() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_CLEAN_CREATE);
        when(transfer.getAmount()).thenReturn(BD_50000);
        when(accountService.existsByTransferId(ID_1)).thenReturn(false);
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        when(factory.createCleanDto(transfer, ACCOUNT)).thenReturn(dto);
        invokePrivate(consumer, METHOD_PROCESS_TRANSFER, PARAMS_PROCESS_TRANSFER, transfer, ACCOUNT,
                (TransferConsumer.ExistsChecker) accountService::existsByTransferId);
        verify(accountService).createTransfer(dto);
        verify(producer).sendVerdict(ID_1, ACCOUNT, KafkaConstants.ALLOWED, PURPOSE_CLEAN_CREATE);
    }

    @Test
    void processTransfer_shouldCleanUpdate_whenAmountNotBlockedAndExists() throws Exception {
        TransferChecked transfer = mock(TransferChecked.class);
        when(transfer.getId()).thenReturn(ID_1);
        when(transfer.getPurpose()).thenReturn(PURPOSE_CLEAN_UPDATE);
        when(transfer.getAmount()).thenReturn(BD_50000);
        when(accountService.existsByTransferId(ID_1)).thenReturn(true);
        when(accountService.findIdByTransferId(ID_1)).thenReturn(Optional.of(ID_123));
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        when(factory.updateDto(transfer, ACCOUNT)).thenReturn(dto);
        invokePrivate(consumer, METHOD_PROCESS_TRANSFER, PARAMS_PROCESS_TRANSFER, transfer, ACCOUNT,
                (TransferConsumer.ExistsChecker) accountService::existsByTransferId);
        verify(accountService).updateTransfer(Optional.of(ID_123), dto);
        verify(producer).sendVerdict(ID_1, ACCOUNT, KafkaConstants.ALLOWED, PURPOSE_CLEAN_UPDATE);
    }

}