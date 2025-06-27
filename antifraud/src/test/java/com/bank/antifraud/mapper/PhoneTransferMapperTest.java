package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.SuspiciousPhoneTransfer;
import com.bank.antifraud.entities.TransferChecked;
import com.bank.antifraud.util.TransferMapperConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PhoneTransferMapperTest {

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

    private final PhoneTransferMapper mapper = Mappers.getMapper(PhoneTransferMapper.class);

    @Test
    void toDto_shouldMapEntityToDto() {
        SuspiciousPhoneTransfer entity = new SuspiciousPhoneTransfer();
        entity.setId(ID_1);
        entity.setIsBlocked(BOOL_TRUE);
        entity.setIsSuspicious(BOOL_TRUE);
        entity.setBlockedReason(REASON_EXCEEDED);
        entity.setSuspiciousReason(REASON_DETECTED);
        entity.setPhoneTransferId(ID_999);
        SuspiciousPhoneTransferDto dto = mapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.getPhoneTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isTrue();
        assertThat(dto.getBlockedReason()).isEqualTo(REASON_EXCEEDED);
    }

    @Test
    void toEntity_shouldMapDtoToEntityWithoutId() {
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setPhoneTransferId(ID_999);
        dto.setIsBlocked(BOOL_FALSE);
        dto.setIsSuspicious(BOOL_FALSE);
        dto.setBlockedReason(REASON_REASON);
        dto.setSuspiciousReason(REASON_NO);
        SuspiciousPhoneTransfer entity = mapper.toEntity(dto);
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getPhoneTransferId()).isEqualTo(ID_999);
        assertThat(entity.getIsBlocked()).isFalse();
    }

    @Test
    void updateFromDto_shouldUpdateEntityFieldsExceptId() {
        SuspiciousPhoneTransferDto dto = new SuspiciousPhoneTransferDto();
        dto.setIsBlocked(BOOL_TRUE);
        dto.setBlockedReason(REASON_UPDATED);
        dto.setSuspiciousReason(REASON_UPDATED);
        SuspiciousPhoneTransfer entity = new SuspiciousPhoneTransfer();
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
        assertNull(mapper.toDto(null));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void updateFromDto_ShouldDoNothing_WhenDtoIsNull() {
        SuspiciousPhoneTransfer entity = new SuspiciousPhoneTransfer();
        assertDoesNotThrow(() -> mapper.updateFromDto(null, entity));
    }

    @Test
    void eventToDto_shouldMapCheckedEventToDto() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousPhoneTransferDto dto = mapper.eventToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getPhoneTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_DETECTED);
    }

    @Test
    void updateToDto_shouldMapCheckedEventToNotBlocked() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousPhoneTransferDto dto = mapper.updateToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getPhoneTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.NOT_BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.NOT_SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
    }

    @Test
    void eventCleanToDto_shouldMapCheckedEventToNotBlocked() {
        TransferChecked event = new TransferChecked();
        event.setId(ID_999);
        SuspiciousPhoneTransferDto dto = mapper.eventCleanToDto(event);
        assertThat(dto).isNotNull();
        assertThat(dto.getPhoneTransferId()).isEqualTo(ID_999);
        assertThat(dto.getIsBlocked()).isEqualTo(TransferMapperConstants.NOT_BLOCKED);
        assertThat(dto.getIsSuspicious()).isEqualTo(TransferMapperConstants.NOT_SUSPICIOUS);
        assertThat(dto.getBlockedReason()).isEqualTo(TransferMapperConstants.BLOCKED_REASON_NOT_EXCEEDED);
        assertThat(dto.getSuspiciousReason()).isEqualTo(TransferMapperConstants.SUSPICIOUS_REASON_NOT_DETECTED);
    }
}
