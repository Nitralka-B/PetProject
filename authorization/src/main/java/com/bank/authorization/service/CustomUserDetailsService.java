package com.bank.authorization.service;

import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.validate.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для загрузки данных пользователя в Spring Security.
 * Реализует поиск пользователя по его profileId в базе данных
 * и преобразование в объект UserDetails, необходимый для аутентификации.
 * В случае отсутствия пользователя выбрасывает UsernameNotFoundException.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public UserDetails loadUserByUsername(String profileId) throws UsernameNotFoundException {

        final long id;
        try {
            id = Long.parseLong(profileId);
            userValidator.validateProfileId(id);
        } catch (NumberFormatException e) {
            log.warn("Некорректный формат ProfileId: {}", profileId);
            throw new UsernameNotFoundException("ProfileId должен быть числом");
        }
        return userRepository.findByProfileId(id)
                .map(user -> {
                    log.info("Успешно загружен пользователь: {}", id);
                    return new CustomUserDetails(user);
                })
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + id));
    }
}
