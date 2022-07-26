package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Events, Long>, EventsCustomRepository {

    List<Events> findByAccountsId(Long accountsId);

    List<Events> findByEventsTimeGreaterThan(LocalDateTime localDateTime);

    @Query("SELECT DISTINCT e " +
            "  FROM Events e " +
            "  LEFT JOIN FETCH e.eventsImagesSet " +
            "  LEFT JOIN FETCh e.tagsSet " +
            "  LEFT JOIN FETCh e.likesSet " +
            "  LEFT JOIN FETCh e.accountsEventsSet " +
            "  LEFT JOIN FETCh e.accounts" +
            "  WHERE e.id = :id")
    Optional<Events> findById(Long id);

}
