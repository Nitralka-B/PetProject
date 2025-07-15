package com.bank.transfer.services;

import com.bank.transfer.DTO.AccountDetailsDto;

import java.util.Map;

public interface AccountDetailsService {
    void processAccountDetails(AccountDetailsDto dto);
    Map<Long, AccountDetailsDto> getAccountCache();
}
