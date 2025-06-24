package com.bank.publicinfo.kafka.consumer;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.BankResponseDto;
import com.bank.publicinfo.kafka.producer.BankKafkaProducer;
import com.bank.publicinfo.service.BankDetailsService;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link BankGetConsumer}.
 * Проверяют обработку запросов на получение банковских данных через Kafka.
 */
@ExtendWith(MockitoExtension.class)
class BankGetConsumerTest {

    /** Мок сервиса для работы с банковскими реквизитами */
    @Mock
    private BankDetailsService bankDetailsService;

    /** Мок продюсера для отправки ответов */
    @Mock
    private BankKafkaProducer producer;

    /** Тестируемый потребитель сообщений */
    @InjectMocks
    private BankGetConsumer bankGetConsumer;

    /** Каптор для перехвата отправляемых ответов */
    @Captor
    private ArgumentCaptor<BankResponseDto> responseCaptor;

    /**
     * Проверяет обработку валидного запроса с корректным ID.
     */
    @Test
    void handleGetRequest_ShouldProcessValidRequest() {
        BankDetailsDto expectedDto = TestUtil.createTestBankDetailsDto();
        when(bankDetailsService.getById(TestConstants.TEST_ID)).thenReturn(expectedDto);

        bankGetConsumer.handleGetRequest(TestConstants.VALID_BANK_ID_STRING);

        verify(bankDetailsService).getById(TestConstants.TEST_ID);
        verify(producer).sendBankResponse(responseCaptor.capture());

        BankResponseDto response = responseCaptor.getValue();
        assertNotNull(response.getRequestId());
        assertEquals(expectedDto, response.getBankDetails());
        assertEquals(TestConstants.SUCCESS_STATUS, response.getStatus());
        assertNotNull(response.getResponseTimestamp());
    }

    /**
     * Проверяет обработку запроса с некорректным форматом ID.
     */
    @Test
    void handleGetRequest_ShouldHandleInvalidNumberFormat() {
        bankGetConsumer.handleGetRequest(TestConstants.INVALID_BANK_ID_STRING);

        verify(bankDetailsService, never()).getById(anyLong());
        verify(producer).sendBankResponse(responseCaptor.capture());

        BankResponseDto response = responseCaptor.getValue();
        assertNotNull(response.getRequestId());
        assertNull(response.getBankDetails());
        assertTrue(response.getStatus().contains(TestConstants.INVALID_ID_FORMAT_MESSAGE));
        assertNotNull(response.getResponseTimestamp());
    }

    /**
     * Проверяет обработку ошибки при получении данных из сервиса.
     */
    @Test
    void handleGetRequest_ShouldHandleServiceException() {
        when(bankDetailsService.getById(TestConstants.TEST_ID))
                .thenThrow(new RuntimeException(TestConstants.SERVICE_ERROR_MESSAGE));

        bankGetConsumer.handleGetRequest(TestConstants.VALID_BANK_ID_STRING);

        verify(producer).sendBankResponse(responseCaptor.capture());

        BankResponseDto response = responseCaptor.getValue();
        assertNotNull(response.getRequestId());
        assertNull(response.getBankDetails());
        assertNotEquals(TestConstants.SUCCESS_STATUS, response.getStatus());
        assertNotNull(response.getResponseTimestamp());
    }

    /**
     * Проверяет обработку различных валидных форматов ID.
     */
    @Test
    void handleGetRequest_ShouldParseValidBankIds() {
        when(bankDetailsService.getById(TestConstants.TEST_ID)).thenReturn(new BankDetailsDto());

        assertAll(
                () -> assertDoesNotThrow(() ->
                        bankGetConsumer.handleGetRequest(TestConstants.VALID_BANK_ID_STRING)),
                () -> assertDoesNotThrow(() ->
                        bankGetConsumer.handleGetRequest(TestConstants.BANK_ID_WITH_SPECIAL_CHARS)),
                () -> assertDoesNotThrow(() ->
                        bankGetConsumer.handleGetRequest(TestConstants.BANK_ID_WITH_HYPHENS))
        );
    }

    /**
     * Проверяет обработку невалидных ID.
     */
    @Test
    void handleGetRequest_ShouldFailForInvalidBankIds() {
        assertDoesNotThrow(() -> bankGetConsumer.handleGetRequest(TestConstants.INVALID_BANK_ID_STRING));
        assertDoesNotThrow(() -> bankGetConsumer.handleGetRequest(TestConstants.ONLY_SPECIAL_CHARS));

        verify(producer, times(2)).sendBankResponse(argThat(response ->
                response.getStatus().contains(TestConstants.INVALID_ID_FORMAT_MESSAGE)));
    }

    /**
     * Проверяет обработку пустого запроса.
     */
    @Test
    void handleGetRequest_ShouldHandleEmptyString() {
        assertDoesNotThrow(() -> bankGetConsumer.handleGetRequest(""));

        verify(producer).sendBankResponse(argThat(response ->
                response != null &&
                        response.getStatus() != null &&
                        response.getStatus().contains(TestConstants.INVALID_ID_FORMAT_MESSAGE)));
    }
}
