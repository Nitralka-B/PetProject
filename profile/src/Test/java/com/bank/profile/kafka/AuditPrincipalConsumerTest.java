package com.bank.profile.kafka;

import com.bank.profile.DTO.PrincipalUserDto;
import com.bank.profile.Exceptions.NotAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuditPrincipalConsumerTest {

    private AuditPrincipalConsumer auditPrincipalConsumer;

    @BeforeEach
    void setUp() {
        auditPrincipalConsumer = new AuditPrincipalConsumer();
    }

    @Test
    void getAuditPrincipal_ShouldSetPrincipalCorrectly() {
        PrincipalUserDto principal = new PrincipalUserDto();
        principal.setId(1L);
        principal.setUsername("testUser");

        auditPrincipalConsumer.getAuditPrincipal(principal);

        assertEquals(principal, auditPrincipalConsumer.getPrincipal());
    }

    @Test
    void getPrincipal_ShouldThrowWhenNotAuthorized() {
        assertThrows(NotAuthorizedException.class, () -> auditPrincipalConsumer.getPrincipal());
    }

}
