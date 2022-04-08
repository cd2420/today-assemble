package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EventsCustomRepository {

    PageImpl<Events> getEventsList(Pageable pageable);

    PageImpl<Events> getEventsList(Pageable pageable, LocalDateTime localDateTime);
}
