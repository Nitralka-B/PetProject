package com.bank.transfer.services;

import com.bank.transfer.DTO.AuditDTO;


public interface AuditService {
    void logAudit(AuditDTO auditDTO);
    AuditDTO findFirstAudit(String entityType, Long id);
    Object findDtoById(String entityType, Long id);

}
