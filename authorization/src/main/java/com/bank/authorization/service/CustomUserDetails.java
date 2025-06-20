package com.bank.authorization.service;


import com.bank.authorization.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса UserDetails для интеграции с системой безопасности.
 * Оборачивает объект User и предоставляет необходимые данные для аутентификации и авторизации.
 */
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }


    @Override
    public String getPassword() {
        if (user.getPassword() == null) {
            throw new IllegalStateException("Пароль не может быть null");
        }
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return String.valueOf(user.getProfileId()); // Используем profileId как username
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
