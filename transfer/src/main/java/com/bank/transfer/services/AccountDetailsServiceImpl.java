package com.bank.transfer.services;

import com.bank.transfer.DTO.AccountDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
@Slf4j
public class AccountDetailsServiceImpl implements AccountDetailsService {
    private final Map<Long, AccountDetailsDto> accountDetailsCache = new ConcurrentHashMap<>();
    private final Map<Long, AccountDetailsDto> accountCache = new ConcurrentHashMap<>();



    @Override
    public AccountDetailsDto getById(Long accountDetailsId) {
        System.out.println(accountCache.get(accountDetailsId));
        return accountCache.get(accountDetailsId);
    }


    @Override
    public void processAccountDetails(AccountDetailsDto dto) {
        accountDetailsCache.put(dto.getId(), dto); // ключ — ID счёта
        log.info("Сохранены в кэш данные счёта: {}", dto);
    }

    @Override
    public Map<Long, AccountDetailsDto> getAccountCache() {
        return accountDetailsCache;
    }
}
