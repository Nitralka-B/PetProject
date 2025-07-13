package com.bank.account.validator;

import com.bank.account.dto.AccountDto;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UniqueAccountDataValidator implements ConstraintValidator<UniqueAccountData, AccountDto> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public boolean isValid(AccountDto accountDto, ConstraintValidatorContext constraintValidatorContext) {
        log.info(">>>>> Вызван валидатор UniqueAccountData <<<<<");
        if (accountDto != null) {
            final AccountDto foundInDataBaseByNumber =
                    accountRepository.findByAccountNumber(accountDto.getAccountNumber())
                    .map(accountMapper::toDto)
                    .orElse(null);
            final AccountDto foundInDataBaseByBank =
                    accountRepository.findByBankDetailsId(accountDto.getBankDetailsId())
                    .map(accountMapper::toDto)
                    .orElse(null);
            if (foundInDataBaseByNumber == null && foundInDataBaseByBank == null) {
                return true;
            } else {
                log.error("Найден дубликат аккаунта при попытке создания!!!");
            }
        }
        return false;
    }
}
