package com.bank.profile.Services;

import com.bank.profile.Entities.AccountDetailsId;
import com.bank.profile.Entities.Profile;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Repositories.AccountDetailsIdRepository;
import com.bank.profile.Repositories.ProfileRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса AccountDetailsIdService
 */
@Service
@RequiredArgsConstructor
public class AccountDetailsServiceImpl implements AccountDetailsService {

    private final ProfileRepository profileRepository;
    private final AccountDetailsIdRepository accountDetailsIdRepository;

    /**
     * Метод отвечающий за создание записи
     * @param profileId - Id профиля
     * @param accountId - Id аккаунта
     */
    @Override
    public void createAccountDetails(Long profileId, Long accountId) {
        try {
            if (profileId == null || accountId == null) {
                throw new EntityNotFoundException("Profile id or account id is null");
            }

            if (profileRepository.findById(profileId) == null) {
                throw new EntityExistsException("Profile already exists");
            }
            final Profile profile = profileRepository.findById(profileId);

            final AccountDetailsId accountDetailsId = new AccountDetailsId();
            accountDetailsId.setAccountId(accountId);
            accountDetailsId.setProfile(profile);

            accountDetailsIdRepository.save(accountDetailsId);
        } catch (EntityNotFoundException e) {
            throw e;
        }
    }
}
