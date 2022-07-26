package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface EventsCustomRepository {

    PageImpl<Events> getEventsList(Pageable pageable, LocalDateTime localDateTime);

    PageImpl<Events> findByKeyword(Pageable pageable, String keyword, LocalDateTime localDateTime);

    Integer findByKeywordSize(String keyword, LocalDateTime localDateTime);

    PageImpl<Events> findByPlace(Pageable pageable, String keyword, LocalDateTime localDateTime);

    Integer findByPlaceSize(String keyword, LocalDateTime localDateTime);

}
