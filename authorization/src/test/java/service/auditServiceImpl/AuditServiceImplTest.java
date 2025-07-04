package service.auditServiceImpl;


import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.Audit;
import com.bank.authorization.mapper.AuditMapper;
import com.bank.authorization.repository.AuditRepository;
import com.bank.authorization.service.AuditServiceImpl;
import com.bank.authorization.util.SecurityContextUtil;
import com.bank.authorization.validate.AuditValidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.bank.authorization.entity.OperationType.CREATE;
import static com.bank.authorization.entity.OperationType.UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;
    @Mock
    private AuditMapper auditMapper;
    @Mock
    private SecurityContextUtil securityContextUtil;
    @Mock
    private AuditValidate auditValidate;

    @InjectMocks
    private AuditServiceImpl auditService;

    private static final AuditDto AUDIT_DTO = new AuditDto();
    private static final String CURRENT_USER = "testUser";

    @Test
    @DisplayName("Успешное логирование CREATE операции")
    void logCreateOperation() {
        Audit expectedAudit = new Audit();
        when(auditMapper.toEntity(AUDIT_DTO)).thenReturn(expectedAudit);
        when(securityContextUtil.getCurrentUsername()).thenReturn(CURRENT_USER);

        AUDIT_DTO.setOperationType(CREATE);
        auditService.log(AUDIT_DTO);

        verify(auditMapper).toEntity(AUDIT_DTO);
        assertEquals(CURRENT_USER, expectedAudit.getCreatedBy());
        assertNotNull(expectedAudit.getCreatedAt());

        verify(auditRepository).save(expectedAudit);
    }

    @Test
    @DisplayName("Успешное логирование UPDATE операции")
    void logUpdateOperation() {

        Audit expectedAudit = new Audit();
        when(auditMapper.toEntity(AUDIT_DTO)).thenReturn(expectedAudit);
        when(securityContextUtil.getCurrentUsername()).thenReturn(CURRENT_USER);

        AUDIT_DTO.setOperationType(UPDATE);
        auditService.log(AUDIT_DTO);

        assertEquals(CURRENT_USER, expectedAudit.getModifiedBy());
        assertNotNull(expectedAudit.getModifiedAt());
        verify(auditRepository).save(expectedAudit);
    }

    @Test
    @DisplayName("Логирование с пустым пользователем в контексте")
    void logEmptyContextHolder() {

        Audit expectedAudit = new Audit();
        when(auditMapper.toEntity(AUDIT_DTO)).thenReturn(expectedAudit);
        when(securityContextUtil.getCurrentUsername()).thenReturn(null);

        auditService.log(AUDIT_DTO);

        assertNull(expectedAudit.getCreatedBy());
        verify(auditRepository).save(expectedAudit);
    }

}
