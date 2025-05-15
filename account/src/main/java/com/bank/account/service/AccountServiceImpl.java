package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.Account;
import com.bank.account.exception.DataSaveException;
import com.bank.account.exception.DataUpdateException;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.util.AccountTopic;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Реализация интерфейса {@link AccountService}, предоставляющая бизнес-логику
 * для управления учетными записями.
 * <p>
 * Отвечает за создание, обновление, удаление и получение учетных записей,
 * а также за отправку соответствующих событий в Kafka.
 * <p>
 * Класс помечен аннотацией {@code @Transactional}, что обеспечивает автоматическое
 * управление транзакциями при выполнении методов.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements  AccountService{
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountEventService accountEventService;

    /**
     * Получает учетную запись по номеру.
     *
     * @param number номер учетной записи
     * @return объект {@link AccountDto}, если найден; иначе {@code null}
     */
    @Override
    public AccountDto getAccountByNumber(Long number) {
        return accountRepository.findByAccountNumber(number)
                .map(accountMapper::toDto)
                .orElse(null);
    }
    /**
     * Добавляет новую учетную запись и отправляет событие в Kafka.
     *
     * @param accountDto DTO создаваемой учетной записи
     */
    @Transactional
    @Override
    public void addAccount(AccountDto accountDto) {
        try {
            log.info(">> DataBase | Adding account {}", accountDto.getAccountNumber());
            accountEventService.messageToKafka(AccountTopic.CREATE, accountMapper.
                    toDto(accountRepository.save(accountMapper.toEntity(accountDto))));
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при сохранении аккаунта: {}. DTO: {}", e.getMessage(), accountDto, e);
            throw new DataSaveException("Ошибка сохранения: проверьте обязательные и уникальные поля.");
        }
    }
    /**
     * Обновляет существующую учетную запись и отправляет событие в Kafka.
     *
     * @param accountDto DTO с обновленными данными учетной записи
     */
    @Transactional
    @Override
    public void updateAccount(AccountDto accountDto) {
        try {
            accountRepository.findById(accountDto.getId()).ifPresentOrElse(account -> {
                accountMapper.updateAccountFromDto(accountDto, account);
                log.info(">> DataBase | Updating account " + accountDto.getId());
                accountRepository.save(account);
                accountEventService.messageToKafka(AccountTopic.UPDATE, accountDto);
            }, () -> {
                throw new DataUpdateException("Аккаунт с ID " + accountDto.getId() + " не найден.");
            });
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при обновлении аккаунта: {}. DTO: {}", e.getMessage(), accountDto, e);
            throw new DataUpdateException("Ошибка сохранения: проверьте обязательные и уникальные поля.");
        }
    }
    /**
     * Возвращает список всех учетных записей и отправляет событие в Kafka.
     *
     * @return список объектов {@link AccountDto}
     */
    @Override
    public List<AccountDto> listAccounts() {
        accountEventService.messageToKafka(AccountTopic.GET,null);
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }
    /**
     * Удаляет учетную запись по идентификатору и отправляет событие в Kafka.
     *
     * @param id идентификатор учетной записи
     */
    @Transactional
    @Override
    public void deleteAccount(Long id) {
        log.info(">> DataBase | Deleting account " + id);
        Optional<Account> accountOpt = accountRepository.findById(id);

        accountOpt.ifPresentOrElse(
                account -> {
                    AccountDto accountDto = accountMapper.toDto(account);
                    accountRepository.deleteById(id);
                    accountEventService.messageToKafka(AccountTopic.DELETE, accountDto);
                    },
                () -> log.error(">> DataBase | Deleting non existent account  " + id)
                );
    }
}
