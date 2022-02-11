package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
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
            @RequestBody @Valid UpdateEventsContentsReq updateEventsContentsReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsContentsReq
        );

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsContentsReq, accounts));
    }

    @PutMapping("/tags")
    public ResponseEntity<EventsDto> updateEventsTags(
            @RequestBody @Valid UpdateEventsTagsReq updateEventsTagsReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsTagsReq
        );

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsTagsReq, accounts));
    }

    @PutMapping("/images")
    public ResponseEntity<EventsDto> updateEventsImages(
            @RequestBody @Valid UpdateEventsImagesReq updateEventsImagesReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsImagesReq
        );

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsImagesReq, accounts));
    }

    @PutMapping("/type")
    public ResponseEntity<EventsDto> updateEventsType(
            @RequestBody @Valid UpdateEventsTypeReq updateEventsTypeReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsTypeReq
        );

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsTypeReq, accounts));
    }

    @DeleteMapping("/{eventsId}")
    public ResponseEntity<Void> deleteEvents(
            @PathVariable Long eventsId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("url : {}"
                , request.getRequestURI()
        );
        eventsService.deleteEvents(eventsId, accounts);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/{eventsId}/accounts")
    public ResponseEntity<AccountsEventsDto<AccountsDto>> participateEvents(
            @PathVariable Long eventsId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}"
                , request.getRequestURI()
        );

        return ResponseEntity.ok(eventsService.participateEventsManage(eventsId, accounts));
    }

    @PostMapping("/{eventsId}/accounts/{accountsId}")
    public ResponseEntity<AccountsEventsDto<EventsDto>> inviteEvents(
            @PathVariable Long eventsId
            , @PathVariable Long accountsId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}"
                , request.getRequestURI()
        );

        return ResponseEntity.ok(eventsService.inviteEvents(eventsId, accounts, accountsId));
    }
}
