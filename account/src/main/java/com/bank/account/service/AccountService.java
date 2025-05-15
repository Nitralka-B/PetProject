package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import java.util.List;

/**
 * Интерфейс, определяющий контракт сервисного слоя для управления учетными записями (Account).
 * <p>
 * Предоставляет операции создания, обновления, удаления, получения и списка учетных записей.
 */
public interface AccountService {
    /**
     * Получает учетную запись по номеру счета.
     *
     * @param id номер счета (ID)
     * @return DTO объекта учетной записи
     */
    AccountDto getAccountByNumber(Long id);
    /**
     * Удаляет учетную запись по ID.
     *
     * @param id идентификатор учетной записи
     */
    void deleteAccount(Long id);
    /**
     * Добавляет новую учетную запись.
     *
     * @param accountDto DTO новой учетной записи
     */
    void addAccount(AccountDto accountDto);
    /**
     * Обновляет существующую учетную запись.
     *
     * @param accountDto DTO с обновленными данными учетной записи
     */
    void updateAccount(AccountDto accountDto);
    /**
     * Возвращает список всех учетных записей.
     *
     * @return список DTO всех учетных записей
     */
    List<AccountDto> listAccounts();
}
