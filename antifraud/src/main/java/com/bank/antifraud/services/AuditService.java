package com.bank.antifraud.services;
import com.bank.antifraud.dto.AuditDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AuditService {
    void logAudit(AuditDto auditDto);

    List<AuditDto> getAllAuditLogs();

    Object findDtoById(String entityType, Long id);

    AuditDto findLastAudit(String entityType, Long entityId);

    /**
     * Возвращает самую первую (CREATE) запись аудита для указанного transferId.
     * Используется для того, чтобы не терять первоначальные createdBy / createdAt при UPDATE.
     */
    AuditDto findFirstAudit(String entityType, Long transferId);
}
