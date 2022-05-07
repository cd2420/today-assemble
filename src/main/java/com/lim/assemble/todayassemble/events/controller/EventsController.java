package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private final EventsService eventsService;

    @GetMapping("")
    public ResponseEntity<List<EventsDto>> getEventsList(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
            , HttpServletRequest request
            ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @GetMapping("/size")
    public ResponseEntity<Integer> getEventsListSize(HttpServletRequest request) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEventsListSize());
    }

    @GetMapping("{eventId}")
    public ResponseEntity<EventsDto> getEventsDetail(
            @PathVariable Long eventId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEvents(eventId));
    }

    @GetMapping("{eventId}/accounts")
    public ResponseEntity<Integer> getParticipateEventsAccounts(
            @PathVariable Long eventId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getParticipateEventsAccounts(eventId));
    }
}
