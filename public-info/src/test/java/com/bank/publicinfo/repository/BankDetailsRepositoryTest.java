package com.bank.publicinfo.repository;

import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankDetailsRepositoryTest {

    @Mock
    private BankDetailsRepository repository;

    @Test
    void findByInn_ShouldReturnBankDetails_WhenExists() {
        BankDetails expectedDetails = TestUtil.createTestBankDetails();

        when(repository.findByInn(TestConstants.TEST_INN)).thenReturn(Optional.of(expectedDetails));

        Optional<BankDetails> actualDetails = repository.findByInn(TestConstants.TEST_INN);

        assertThat(actualDetails)
                .isPresent()
                .hasValueSatisfying(details ->
                        assertThat(details.getInn()).isEqualTo(TestConstants.TEST_INN));
        verify(repository).findByInn(TestConstants.TEST_INN);
    }

    @Test
    void existsByBik_ShouldReturnTrue_WhenExists() {
        when(repository.existsByBik(TestConstants.TEST_BIK)).thenReturn(true);

        assertThat(repository.existsByBik(TestConstants.TEST_BIK)).isTrue();
        verify(repository).existsByBik(TestConstants.TEST_BIK);
    }

    @Test
    void saveWithLog_ShouldLogAndSaveCorrectly() {
        BankDetails newDetails = TestUtil.createNewBankDetails();
        BankDetails savedDetails = TestUtil.createTestBankDetails();

        BankDetailsRepository repoMock = mock(BankDetailsRepository.class, CALLS_REAL_METHODS);
        when(repoMock.save(newDetails)).thenReturn(savedDetails);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            BankDetails result = repoMock.saveWithLog(newDetails);

            assertThat(result)
                    .isNotNull()
                    .extracting(BankDetails::getId)
                    .isEqualTo(TestConstants.TEST_ID);
            verify(repoMock).save(newDetails);

            assertThat(outContent.toString())
                    .contains(TestConstants.LOG_SAVING_BANK_DETAILS)
                    .contains(TestConstants.LOG_INN_PREFIX + TestConstants.TEST_INN);
        } finally {
            System.setOut(originalOut);
        }
    }
}
