package com.bank.publicinfo.kafka.consumer;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.service.BankDetailsService;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link BankKafkaConsumer}.
 * Проверяет обработку сообщений Kafka для операций создания, обновления и удаления банков.
 */
@ExtendWith(MockitoExtension.class)
class BankKafkaConsumerTest {

    @Mock
    private BankDetailsService bankDetailsService;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private BankKafkaConsumer bankKafkaConsumer;

    private BankDetailsDto testBankDetails;

    /**
     * Инициализирует тестовые данные перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        testBankDetails = TestUtil.createTestBankDetailsDto();
    }

    /**
     * Проверяет создание нового банка при отсутствии банка с таким БИК:
     */
    @Test
    void consumeCreate_ShouldCreateNewBank_WhenBikNotExists() {
        when(bankDetailsService.existsByBik(testBankDetails.getBik())).thenReturn(false);

        bankKafkaConsumer.consumeCreate(testBankDetails, TestConstants.TEST_METHOD_SIGNATURE, acknowledgment);

        verify(bankDetailsService).create(testBankDetails);
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет пропуск создания при существующем БИК:
     */
    @Test
    void consumeCreate_ShouldSkipCreation_WhenBikExists() {
        when(bankDetailsService.existsByBik(testBankDetails.getBik())).thenReturn(true);

        bankKafkaConsumer.consumeCreate(testBankDetails, TestConstants.TEST_METHOD_SIGNATURE, acknowledgment);

        verify(bankDetailsService, never()).create(testBankDetails);
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет подтверждение сообщения при возникновении исключения:
     */
    @Test
    void consumeCreate_ShouldAcknowledge_WhenExceptionThrown() {
        when(bankDetailsService.existsByBik(testBankDetails.getBik()))
                .thenThrow(new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE));

        bankKafkaConsumer.consumeCreate(testBankDetails, TestConstants.TEST_METHOD_SIGNATURE, acknowledgment);

        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет обновление существующего банка:
     */
    @Test
    void consumeUpdate_ShouldUpdateBank_WhenBankExists() {
        when(bankDetailsService.existsById(TestConstants.TEST_ID)).thenReturn(true);

        bankKafkaConsumer.consumeUpdate(testBankDetails, TestConstants.VALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService).update(TestConstants.TEST_ID, testBankDetails);
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет пропуск обновления при отсутствии банка:
     */
    @Test
    void consumeUpdate_ShouldSkipUpdate_WhenBankNotExists() {
        when(bankDetailsService.existsById(TestConstants.TEST_ID)).thenReturn(false);

        bankKafkaConsumer.consumeUpdate(testBankDetails, TestConstants.VALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService, never()).update(anyLong(), any());
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет обработку невалидного ID банка:
     */
    @Test
    void consumeUpdate_ShouldHandleInvalidBankIdFormat() {
        bankKafkaConsumer.consumeUpdate(testBankDetails, TestConstants.INVALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService, never()).update(anyLong(), any());
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет удаление существующего банка:
     */
    @Test
    void handleDelete_ShouldDeleteBank_WhenBankExists() {
        when(bankDetailsService.existsById(TestConstants.TEST_ID)).thenReturn(true);

        bankKafkaConsumer.handleDelete(TestConstants.VALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService).delete(TestConstants.TEST_ID);
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет пропуск удаления при отсутствии банка:
     */
    @Test
    void handleDelete_ShouldSkipDelete_WhenBankNotExists() {
        when(bankDetailsService.existsById(TestConstants.TEST_ID)).thenReturn(false);

        bankKafkaConsumer.handleDelete(TestConstants.VALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService, never()).delete(anyLong());
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет обработку невалидного ID банка:
     */
    @Test
    void handleDelete_ShouldHandleInvalidBankIdFormat() {
        bankKafkaConsumer.handleDelete(TestConstants.INVALID_BANK_ID_STRING, acknowledgment);

        verify(bankDetailsService, never()).delete(anyLong());
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет повторное выбрасывание исключения при ошибке удаления:
     */
    @Test
    void handleDelete_ShouldRethrowException_WhenErrorOccurs() {
        when(bankDetailsService.existsById(TestConstants.TEST_ID)).thenReturn(true);
        doThrow(new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE))
                .when(bankDetailsService).delete(TestConstants.TEST_ID);

        assertThrows(RuntimeException.class,
                () -> bankKafkaConsumer.handleDelete(TestConstants.VALID_BANK_ID_STRING, acknowledgment));
        verify(acknowledgment).acknowledge();
    }

    /**
     * Проверяет валидацию null DTO при создании:
     */
    @Test
    void consumeCreate_ShouldThrowIllegalArgumentException_WhenDtoIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> bankKafkaConsumer.consumeCreate(null, TestConstants.TEST_METHOD_SIGNATURE, acknowledgment));
    }

    /**
     * Проверяет валидацию null key при создании:
     */
    @Test
    void consumeCreate_ShouldThrowIllegalArgumentException_WhenKeyIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> bankKafkaConsumer.consumeCreate(testBankDetails, null, acknowledgment));
    }
}
