package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepository extends JpaRepository<Events, Long> {
}
