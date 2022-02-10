package com.lim.assemble.todayassemble.accounts.repository;

import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsEventsRepository extends JpaRepository<AccountsMapperEvents, Long> {
    Optional<AccountsMapperEvents> findByAccountsIdAndEventsId(Long accountsId, Long eventsId);
}
