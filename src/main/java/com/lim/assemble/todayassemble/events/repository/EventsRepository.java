package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<Events, Long>, EventsCustomRepository {

    List<Events> findByAccountsId(Long accountsId);

    List<Events> findByEventsTimeGreaterThan(LocalDateTime localDateTime);
}
