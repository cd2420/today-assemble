package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Long> {
}
