package com.bank.profile.Repositories;

import com.bank.profile.Entities.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Integer> {
    Passport getById(Long id);
}
