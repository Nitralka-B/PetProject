package com.bank.account.repository;

import com.bank.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Репозиторий для управления сущностями {@link Account}.
 * <p>
 * Расширяет {@link JpaRepository}, предоставляя базовые CRUD-операции.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Ищет аккаунт по номеру счёта.
     *
     * @param accountNumber номер счёта
     * @return {@link Optional} с найденным аккаунтом, либо пустой, если не найден
     */
    Optional<Account> findByAccountNumber(Long accountNumber);

    Optional<Account> findByBankDetailsId(Long bankDetailsId);
}
