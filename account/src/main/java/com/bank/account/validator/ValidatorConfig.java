package com.bank.account.validator;

import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.validator.UniqueAccountDataValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {
    @Bean
    public UniqueAccountDataValidator uniqueAccountDataValidator(AccountRepository repo, AccountMapper mapper) {
        return new UniqueAccountDataValidator(repo, mapper);
    }
}
