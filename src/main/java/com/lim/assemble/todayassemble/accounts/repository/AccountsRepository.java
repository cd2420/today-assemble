package com.lim.assemble.todayassemble.accounts.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

//    @Query("SELECT distinct a FROM Accounts a left join fetch a.accountsEventsSet e where a.email = :email")
    <T> Optional<T> findByEmail(String email, Class<T> type);
}
