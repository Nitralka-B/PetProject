package com.bank.authorization.service;

import com.bank.authorization.dto.AuditDto;


/**
 * Сервис для логирования аудиторских событий.
 * Обеспечивает метод для записи информации об операциях.
 */
public interface AuditService {

    void log(AuditDto auditDto);


}
