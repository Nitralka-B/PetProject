package com.bank.authorization.repository;

import com.bank.authorization.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Обеспечивает доступ к данным пользователей и поиск по profileId.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProfileId(Long profileId);

}
