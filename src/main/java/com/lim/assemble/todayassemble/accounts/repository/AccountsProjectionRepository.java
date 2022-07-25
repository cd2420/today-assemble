package com.lim.assemble.todayassemble.accounts.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.projection.LoadUserByUsernameAccounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsProjectionRepository extends JpaRepository<Accounts, Long> {

    Optional<LoadUserByUsernameAccounts> findByEmail(String email);
}
