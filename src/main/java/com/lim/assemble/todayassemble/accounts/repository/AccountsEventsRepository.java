package com.lim.assemble.todayassemble.accounts.repository;

import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountsEventsRepository
        extends JpaRepository<AccountsMapperEvents, Long>
//        , AccountsEventsCustomRepository
{
    Optional<AccountsMapperEvents> findByAccountsIdAndEventsId(Long accountsId, Long eventsId);

    Optional<List<AccountsMapperEvents>> findByEventsId(Long eventsId);

    Optional<List<AccountsMapperEvents>> findByAccountsId(Long accountsId);
}
