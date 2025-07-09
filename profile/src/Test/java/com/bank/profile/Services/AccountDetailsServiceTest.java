package com.bank.profile.Services;

import com.bank.profile.Entities.AccountDetailsId;
import com.bank.profile.Entities.Profile;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Repositories.AccountDetailsIdRepository;
import com.bank.profile.Repositories.ProfileRepository;
import com.bank.profile.Utils.TestUtils;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountDetailsServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private AccountDetailsIdRepository accountDetailsIdRepository;

    @InjectMocks
    private AccountDetailsServiceImpl accountDetailsService;

    private static final Long TEST_ACCOUNT_ID = 100L;
    private static final Long TEST_PROFILE_ID = 1L;
    private static final Long TEST_NULL_PROFILE_ID = null;
    private static final Long TEST_NULL_ACCOUNT_ID = null;

    @Test
    public void createAccountDetails_Success() {
        Profile profile = TestUtils.getProfileForAccountDetails();

        when(profileRepository.findById(profile.getId())).thenReturn(profile);

        accountDetailsService.createAccountDetails(profile.getId(), TEST_ACCOUNT_ID);

        verify(profileRepository, times(2)).findById(profile.getId());
        verify(accountDetailsIdRepository, times(1)).save(any(AccountDetailsId.class));
    }

    @Test
    public void createAccountDetails_WhenProfileIdIsNull_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> accountDetailsService.createAccountDetails(TEST_NULL_PROFILE_ID, TEST_ACCOUNT_ID));
    }

    @Test
    public void createAccountDetails_WhenAccountIdIsNull_ShouldThrowException() {
        assertThrows(EntityNotFoundException.class,
                () -> accountDetailsService.createAccountDetails(TEST_PROFILE_ID, TEST_NULL_ACCOUNT_ID));
    }

    @Test
    void createAccountDetails_WhenProfileNotFound_ShouldThrowException() {
        when(profileRepository.findById(TEST_PROFILE_ID)).thenReturn(null);

        assertThrows(EntityExistsException.class,
                () -> accountDetailsService.createAccountDetails(TEST_PROFILE_ID, TEST_ACCOUNT_ID));
    }

}
