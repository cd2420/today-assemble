package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private final EventsService eventsService;

    @GetMapping("")
    public ResponseEntity<List<EventsDto>> getEventsList(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.DESC)
                    Pageable pageable
            ) {
        return ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @GetMapping("/size")
    public ResponseEntity<Integer> getEventsListSize(
            @CurrentAccount Accounts accounts
    ) {
        return ResponseEntity.ok(eventsService.getEventsListSize(accounts));
    }

    @GetMapping("{eventId}")
    public ResponseEntity<EventsDto> getEventsDetail(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventsService.getEvents(eventId));
    }

    @GetMapping("{eventId}/accounts")
    public ResponseEntity<Integer> getParticipateEventsAccounts(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventsService.getParticipateEventsAccounts(eventId));
    }
}
