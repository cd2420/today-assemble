package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventsController {

    private final EventsService eventsService;

    @GetMapping("")
    public ResponseEntity<List<EventsDto>> getEventsList(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC)
                    Pageable pageable
            ) {
        return ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @GetMapping("{eventId}")
    public ResponseEntity<EventsDto> getEventsDetail(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(eventsService.getEvents(eventId));
    }
}
