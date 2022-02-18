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

    /**
     * Events 생성
     */
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

    /**
     * Events 기본내용 수정
     */
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

    /**
     * Events tags 수정
     */
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

    /**
     * Events images 수정
     */
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

    /**
     * Events type 수정
     */
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

    /**
     * Events 삭제
     */
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

    /**
     * Events 참여 관리
     */
    @PostMapping("/{eventsId}/accounts")
    public ResponseEntity<AccountsEventsDto<AccountsDto>> participateEventsManage(
            @PathVariable Long eventsId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}"
                , request.getRequestURI()
        );

        return ResponseEntity.ok(eventsService.participateEventsManage(eventsId, accounts));
    }

    /**
     * Events 초대
     */
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

    /**
     * Events 초대 응답
     */
    @PutMapping("/{eventsId}/accounts")
    public ResponseEntity<AccountsEventsDto<AccountsDto>> responseEventsInvite(
            @PathVariable Long eventsId
            , @CurrentAccount Accounts accounts
            , @RequestBody @Valid UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq
            , HttpServletRequest request
    ) {
        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateAccountsMapperEventsReq
        );

        return ResponseEntity.ok(eventsService.responseEventsInvite(
                eventsId
                , accounts
                , updateAccountsMapperEventsReq)
        );
    }
}
