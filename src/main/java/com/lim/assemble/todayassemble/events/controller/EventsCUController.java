package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/events")
@Slf4j
@RequiredArgsConstructor
public class EventsCUController {

    private final EventsService eventsService;

    @PostMapping("")
    public ResponseEntity<EventsDto> createEvents(
           @RequestBody @Valid CreateEventsReq createEventsReq
           , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , createEventsReq
        );

        return ResponseEntity.ok(eventsService.createEvents(createEventsReq, accounts));
    }

    @PutMapping("")
    public ResponseEntity<EventsDto> updateEvents(
            @RequestBody @Valid UpdateEventsReq updateEventsReq
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsReq
        );

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsReq));
    }

    @PutMapping("/tags")
    public ResponseEntity<EventsDto> updateEventsTags(
            @RequestBody @Valid UpdateEventsTagsReq updateEventsTagsReq
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsTagsReq
        );

        return ResponseEntity.ok(eventsService.updateEventsTags(updateEventsTagsReq));
    }

    @PutMapping("/images")
    public ResponseEntity<EventsDto> updateEventsImages(
            @RequestBody @Valid UpdateEventsImagesReq updateEventsImagesReq
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsImagesReq
        );

        return ResponseEntity.ok(eventsService.updateEventsImages(updateEventsImagesReq));
    }

    @PutMapping("/eventsType")
    public ResponseEntity<EventsDto> updateEventsType(
            @RequestBody @Valid UpdateEventsTypeReq updateEventsTypeReq
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsTypeReq
        );

        return ResponseEntity.ok(eventsService.updateEventsType(updateEventsTypeReq));
    }
}
