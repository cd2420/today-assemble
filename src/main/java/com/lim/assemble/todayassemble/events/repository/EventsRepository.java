package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long> {

    List<Events> findByAccountsId(Long accountsId);
}
