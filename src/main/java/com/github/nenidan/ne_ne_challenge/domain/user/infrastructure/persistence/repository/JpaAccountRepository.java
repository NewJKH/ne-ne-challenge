package com.github.nenidan.ne_ne_challenge.domain.user.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.nenidan.ne_ne_challenge.domain.user.infrastructure.persistence.entity.AccountEntity;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query(value = "SELECT COUNT(*) FROM account WHERE email = :email", nativeQuery = true)
    Long countByEmail(String email);

}
