package com.bank.authorization.mapper;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.dto.UserDto;
import static com.bank.authorization.entity.OperationType.CREATE;
import static com.bank.authorization.entity.Role.USER;

import com.bank.authorization.util.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class CreateAuditMapper {
    private final JsonConverter jsonConverter;
    private final Clock clock;

    public AuditDto map(UserDto userDto) {

        return AuditDto.builder()
                .id(null)
                .entityType(String.valueOf(USER))
                .operationType(CREATE)
                .createdBy(null)
                .modifiedBy(null)
                .createdAt(OffsetDateTime.now(clock))
                .modifiedAt(OffsetDateTime.now(clock))
                .entityJson(jsonConverter.toJson(userDto))
                .newEntityJson(null)
                .build();
    }
}

