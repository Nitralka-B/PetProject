package com.bank.authorization.service;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserGetResponse;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.entity.User;
import com.bank.authorization.exception.EntityAlreadyExistsException;
import com.bank.authorization.exception.EntityNotFoundException;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.util.JwtTokenUtils;
import com.bank.authorization.validate.UserValidator;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления пользователями.
 * Обеспечивает основные операции: создание, получение, удаление и обновление пользователей.
 * Включает бизнес-логику валидации, обработку исключений и логирование операций.
 * Работает с репозиторием пользователей, кодированием паролей и преобразованием DTO.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final JwtTokenUtils jwtTokenUtils;


    @Override
    @Transactional
    public UserDto createUser(@Valid UserCreateRequest request) {
        log.info("Попытка создать пользователя с profileId: {}", request.getProfileId());

        userValidator.validateProfileId(request.getProfileId());
        userValidator.validatePassword(request.getPassword());
        userValidator.validateRole(request.getRole());

        if (userRepository.findByProfileId(request.getProfileId()).isPresent()) {
            throw new EntityAlreadyExistsException("Пользователь с profileId " +
                    request.getProfileId() + " уже существует");
        }
        final User user = userMapper.toEntityFromCreateRequest(request);
        user.setProfileId(request.getProfileId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        final User saveUser = userRepository.save(user);
        final String token = jwtTokenUtils.generateToken(new CustomUserDetails(user));
        log.info("Создан пользователь {}. Токен: Bearer {}", user.getProfileId(), token);

        log.info("Успешно создан пользователь ID: {}, profileId: {}", saveUser.getId(), saveUser.getProfileId());


        return userMapper.toDto(saveUser);
    }

    @Override
    @Transactional
    public UserGetResponse getAllUsers(@Valid UserGetRequest request) {

        log.info("Попытка получения всех пользователей", request);

        final List<User> users;

        if (request.getProfileId() != null) {
            users = userRepository.findByProfileId(request.getProfileId())
                    .map(List::of)
                    .orElseGet(() -> {
                        log.warn("Пользователь с profileId={} не найден", request.getProfileId());
                        return Collections.emptyList();
                    });
        } else {
            users = userRepository.findAll();
            if (!users.isEmpty()) {
                log.debug("Первые 3 пользователя из {}: {}", users.size(),
                        users.stream()
                                .map(u -> String.format("profileId=%d, role=%s", u.getProfileId(), u.getRole()))
                                .collect(Collectors.joining("; ")));
            }
        }

        final List<UserDto> result = users.stream()
                .map(userMapper::toDto)
                .peek(dto -> log.trace("Детали пользователя: {}", dto))
                .toList();

        log.info("Возвращаем {} пользователей", result.size());
        return new UserGetResponse(result);
    }

    @Override
    @Transactional
    public void deleteUser(@Valid @NotNull @Positive Long profileId) {

        log.info("Запрос на удаление пользователя с profileId: {}", profileId);

        userValidator.validateProfileId(profileId);

        final User user = userRepository.findByProfileId(profileId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Пользователь с идентификатором %d не найден", profileId)));

        userRepository.delete(user);

        log.info("Пользователь удален: ID={}, profileId={}", profileId, user.getProfileId());

    }

    @Override
    @Transactional
    public UserDto updateUserRole(@Valid UserUpdateRequest request) {

        log.info("Запрос изменения роли: profileId={}, newRole={}", request.getProfileId(), request.getRole());

        userValidator.validateProfileId(request.getProfileId());
        userValidator.validateRole(request.getRole());

        final User user = userRepository.findByProfileId(request.getProfileId()).orElseThrow(() ->
                new EntityNotFoundException(String.format("Пользователь с идентификатором %d не найден ",
                        request.getProfileId())));

        final UserDto oldUserDto = UserDto.builder()
                .id(user.getId())
                .profileId(user.getProfileId())
                .role(user.getRole())
                .build();


        boolean updated = false;

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            updated = true;
        }
        if (!request.getRole().equals(user.getRole())) {
            user.setRole(request.getRole());
            updated = true;
        }

        if (updated) {
            userRepository.save(user);
            log.info("User updated: {}", request.getProfileId());
        } else {
            log.warn("Никаких изменений для пользователя не обнаружено: {}", request.getProfileId());
        }

        return oldUserDto;
    }


}
