package com.bank.publicinfo.repository;

import com.bank.publicinfo.entity.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для сущности {@link BankDetails}.
 * Предоставляет методы для работы с банковскими реквизитами.
 */
@Repository
public interface BankDetailsRepository extends JpaRepository<BankDetails, Long> {
    /**
     * Находит банковские реквизиты по ИНН.
     * @param inn ИНН банка
     * @return Optional с банковскими реквизитами
     */
    Optional<BankDetails> findByInn(Long inn);

    /**
     * Проверяет существование банка по БИК.
     * @param bik БИК банка
     * @return true если банк существует, false если нет
     */
    boolean existsByBik(Long bik);

    /**
     * Проверяет существование банка по ИНН.
     * @param inn ИНН банка
     * @return true если банк существует, false если нет
     */
    boolean existsByInn(Long inn);

    /**
     * Сохраняет банковские реквизиты с логированием.
     * @param entity сущность банковских реквизитов
     * @return сохраненная сущность
     */
    default BankDetails saveWithLog(BankDetails entity) {
        System.out.println("Saving bank details: " + entity);
        return save(entity);
    }
}