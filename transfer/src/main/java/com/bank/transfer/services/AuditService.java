package com.bank.transfer.services;

import com.bank.transfer.DTO.AuditDTO;


public interface AuditService {
    void logAudit(AuditDTO auditDTO);
    AuditDTO findFirstAudit(String entityType, Long id);
    Object findDtoById(String entityType, Long id);

    AuditDTO buildAuditDtoForCreate(Long id, String entityType, Object newDto);

    AuditDTO buildAuditDtoForUpdate(Long id, String entityType, Object newDto, Object oldDto, AuditDTO oldAudit);

}
