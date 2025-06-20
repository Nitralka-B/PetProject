package com.bank.profile.Repositories;

import com.bank.profile.Entities.AccountDetailsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDetailsIdRepository extends JpaRepository<AccountDetailsId, Integer> {
}
