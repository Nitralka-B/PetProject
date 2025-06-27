package com.bank.antifraud.util;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты утилиты {@link EntityIdResolver}, которая определяет идентификатор
 * (ID) объектов разных типов.
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EntityIdResolverTest {

    private final EntityIdResolver resolver = new EntityIdResolver();
    private static final Long SAMPLE_ID = 42L;
    private static final Long ACCOUNT_DTO_ID = 15L;
    private static final Long CARD_TRANSFER_DTO_ID = 25L;
    private static final Long OTHER_TRANSFER_ID = 999L;

    @Test
    void resolve_returnsLongAsIs() {
        assertThat(resolver.resolve(SAMPLE_ID)).isEqualTo(SAMPLE_ID);
    }

    @Test
    void resolve_usesGetIdMethodWhenPresent() {
        SuspiciousAccountTransferDto dto = new SuspiciousAccountTransferDto();
        dto.setId(ACCOUNT_DTO_ID);
        dto.setAccountTransferId(OTHER_TRANSFER_ID);
        assertThat(resolver.resolve(dto)).isEqualTo(ACCOUNT_DTO_ID);
    }

    @Test
    void resolve_fallsBackToSpecificAccessorWhenIdNull() {
        SuspiciousCardTransferDto dto = new SuspiciousCardTransferDto();
        dto.setCardTransferId(CARD_TRANSFER_DTO_ID);
        assertThat(resolver.resolve(dto)).isEqualTo(CARD_TRANSFER_DTO_ID);
    }

    @Test
    void resolve_returnsNullForUnsupportedObject() {
        assertThat(resolver.resolve(new Object())).isNull();
    }
}