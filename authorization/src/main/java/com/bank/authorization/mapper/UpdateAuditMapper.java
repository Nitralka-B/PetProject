package com.bank.authorization.mapper;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.dto.UserDto;
import static com.bank.authorization.entity.OperationType.UPDATE;
import static com.bank.authorization.entity.Role.ADMIN;

import com.bank.authorization.entity.User;
import com.bank.authorization.util.JsonConverter;
import com.bank.authorization.util.SecurityContextUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class UpdateAuditMapper {
    private final JsonConverter jsonConverter;
    private final SecurityContextUtil securityContextUtil;
    private final Clock clock;

    public AuditDto map(UserDto oldUser, User updatedUser) {
        final UserDto newUserDto = mapToDto(updatedUser);

        return AuditDto.builder()
                .id(null)
                .entityType(String.valueOf(ADMIN))
                .operationType(UPDATE)
                .createdBy(securityContextUtil.getCurrentUsername())
                .modifiedBy(securityContextUtil.getCurrentUsername())
                .createdAt(OffsetDateTime.now(clock))
                .modifiedAt(OffsetDateTime.now(clock))
                .entityJson(jsonConverter.toJson(oldUser))
                .newEntityJson(jsonConverter.toJson(newUserDto))
                .build();
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .profileId(user.getProfileId())
                .role(user.getRole())
                .build();
    }
}
