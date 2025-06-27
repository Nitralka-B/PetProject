package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entities.SuspiciousAccountTransfer;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.util.TransferMapperConstants;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AccountTransferMapperTest {

    private static final long ID_1 = 1L;
    private static final long ID_999 = 999L;
    private static final long ID_123 = 123L;
    private static final boolean BOOL_TRUE = true;
    private static final boolean BOOL_FALSE = false;
    private static final String REASON_EXCEEDED = "Exceeded";
    private static final String REASON_DETECTED = "Detected";
    private static final String REASON_REASON = "Reason";
    private static final String REASON_NO = "No";
    private static final String REASON_UPDATED = "Updated";
    private static final String REASON_OLD = "Old";

    private final AccountTransferMapper mapper = Mappers.getMapper(AccountTransferMapper.class);

    @Test
    void toDto_shouldMapEntityToDto() {
        SuspiciousAccountTransfer entity = new SuspiciousAccountTransfer();
        entity.setId(ID_1);
        entity.setIsBlocked(BOOL_TRUE);
        entity.setIsSuspicious(BOOL_TRUE);
        entity.setBlockedReason(REASON_EXCEEDED);
        entity.setSuspiciousReason(REASON_DETECTED);
        entity.setAccountTransferId(ID_999);
        SuspiciousAccountTransferDto dto = mapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.getAccountTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isTrue();
        assertThat(dto.getBlockedReason()).isEqualTo(REASON_EXCEEDED);

    }

    @Test
    void toEntity_shouldMapDtoToEntityWithoutId() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setAccountTransferId(ID_999);
        dto.setIsBlocked(BOOL_FALSE);
        dto.setIsSuspicious(BOOL_FALSE);
        dto.setBlockedReason(REASON_REASON);
        dto.setSuspiciousReason(REASON_NO);
        SuspiciousAccountTransfer entity = mapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getAccountTransferId()).isEqualTo(ID_999);
        assertThat(entity.getIsBlocked()).isFalse();
    }

    @Test
    void updateFromDto_shouldUpdateEntityFieldsExceptId() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setIsBlocked(BOOL_TRUE);
        dto.setBlockedReason(REASON_UPDATED);
        dto.setSuspiciousReason(REASON_UPDATED);
        SuspiciousAccountTransfer entity = new SuspiciousAccountTransfer();
        entity.setId(ID_123);
        entity.setIsBlocked(BOOL_FALSE);
        entity.setBlockedReason(REASON_OLD);
        entity.setSuspiciousReason(REASON_OLD);
        mapper.updateFromDto(dto, entity);
        assertThat(entity.getId()).isEqualTo(ID_123);
        assertThat(entity.getIsBlocked()).isTrue();
        assertThat(entity.getBlockedReason()).isEqualTo(REASON_UPDATED);
    }

    @Test
    void toDto_ShouldReturnNull_WhenEntityIsNull() {
        AccountTransferMapperImpl mapper = new AccountTransferMapperImpl();
        SuspiciousAccountTransferDto result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        AccountTransferMapperImpl mapper = new AccountTransferMapperImpl();
        SuspiciousAccountTransfer result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    void updateFromDto_ShouldDoNothing_WhenDtoIsNull() {
        AccountTransferMapperImpl mapper = new AccountTransferMapperImpl();
        SuspiciousAccountTransfer entity = new SuspiciousAccountTransfer();
        assertDoesNotThrow(() -> mapper.updateFromDto(null, entity));
    }

    @Test
    void eventToDto_shouldMapCheckedEventToDto() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousAccountTransferDto dto = mapper.eventToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getAccountTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_DETECTED);
    }

    @Test
    void updateToDto_shouldMapCheckedEventToNotBlocked() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousAccountTransferDto dto = mapper.updateToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getAccountTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.NOT_BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.NOT_SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
    }

    @Test
    void eventCleanToDto_shouldMapCheckedEventToNotBlocked() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousAccountTransferDto dto = mapper.eventCleanToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getAccountTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.NOT_BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.NOT_SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
    }
}
