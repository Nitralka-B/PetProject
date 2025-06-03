package com.bank.publicinfo.service;

import com.bank.publicinfo.dto.BankDetailsDto;

/**
 * Сервис для работы с банковскими реквизитами.
 * Предоставляет CRUD-операции и методы проверки существования банков.
 */
public interface BankDetailsService {
    /**
     * Создает новые банковские реквизиты.
     * @param dto DTO с данными банковских реквизитов
     * @return созданный DTO банковских реквизитов
     */
    BankDetailsDto create(BankDetailsDto dto);

    /**
     * Обновляет существующие банковские реквизиты.
     * @param id идентификатор банковских реквизитов
     * @param dto DTO с обновленными данными
     * @return обновленный DTO банковских реквизитов
     */
    BankDetailsDto update(Long id, BankDetailsDto dto);

    /**
     * Получает банковские реквизиты по идентификатору.
     * @param id идентификатор банковских реквизитов
     * @return DTO банковских реквизитов
     */
    BankDetailsDto getById(Long id);

    /**
     * Удаляет банковские реквизиты по идентификатору.
     * @param id идентификатор банковских реквизитов
     */
    void delete(Long id);

    /**
     * Проверяет существование банка по ID.
     * @param id идентификатор банка
     * @return true если банк существует, false если нет
     */
    boolean existsById(Long id);

    /**
     * Проверяет существование банка по БИК.
     * @param bik БИК банка
     * @return true если банк с таким БИК существует, false если нет
     */
    boolean existsByBik(Long bik);
}