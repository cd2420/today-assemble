package com.lim.assemble.todayassemble.accounts.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {
}
