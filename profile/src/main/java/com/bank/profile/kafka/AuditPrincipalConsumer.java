package com.bank.profile.kafka;

import com.bank.profile.DTO.PrincipalUserDto;
import com.bank.profile.Exceptions.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Consumer, который отвечает за получение текущего пользователя
 */
@Service
@RequiredArgsConstructor
public class AuditPrincipalConsumer {
    private PrincipalUserDto principalUserDto;

    @KafkaListener(topics = "${spring.kafka.topics.profile-getPrincipal}",
            groupId = "getPrincipal",
            errorHandler = "profileKafkaErrorHandler",
            properties = {
                    "spring.json.value.default.type: com.bank.profile.DTO.PrincipalUserDto"
            })

    public void getAuditPrincipal(PrincipalUserDto principal) {
        this.principalUserDto = principal;
    }

    public PrincipalUserDto getPrincipal() {
        if (principalUserDto == null) {
            throw new NotAuthorizedException("Not authorized");
        }
        return principalUserDto;
    }
}
