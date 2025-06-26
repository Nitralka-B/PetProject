package com.bank.transfer.services;

import com.bank.transfer.DTO.AccountDetailsDto;

import java.util.Map;

public interface AccountDetailsService {
    AccountDetailsDto getById(Long accountDetailsId);
    void processAccountDetails(AccountDetailsDto dto);
    Map<Long, AccountDetailsDto> getAccountCache();
}
