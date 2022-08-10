package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsContentsReq;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsImagesReq;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/events")
@Slf4j
@RequiredArgsConstructor
public class EventsCUController {

    private final EventsService eventsService;

    @ApiOperation(value = "모임 생성", notes = "JWT 토큰 필수")
    @PostMapping("")
    public ResponseEntity<EventsDto> createEvents(
           @ApiParam(value = "모임 생성 데이터") @RequestBody @Valid CreateEventsReq createEventsReq
           , @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , createEventsReq
        );

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        return ResponseEntity.ok(eventsService.createEvents(createEventsReq, accounts));
    }

    @ApiOperation(value = "모임 수정", notes = "JWT 토큰 필수")
    @PutMapping("")
    public ResponseEntity<EventsDto> updateEvents(
            @ApiParam(value = "모임 수정 데이터 - 이미지 데이터 제외") @RequestBody @Valid UpdateEventsContentsReq updateEventsContentsReq
            , @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsContentsReq
        );

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsContentsReq, accounts));
    }

    @ApiOperation(value = "모임 이미지 관리", notes = "JWT 토큰 필수")
    @PutMapping("/images")
    public ResponseEntity<EventsDto> updateEventsImages(
            @ApiParam(value = "모임 이미지 데이터") @RequestBody @Valid UpdateEventsImagesReq updateEventsImagesReq
            , @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}, data : {}"
                , request.getRequestURI()
                , updateEventsImagesReq
        );

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        return ResponseEntity.ok(eventsService.updateEvents(updateEventsImagesReq, accounts));
    }

    @ApiOperation(value = "모임 삭제", notes = "JWT 토큰 필수")
    @DeleteMapping("/{eventsId}")
    public ResponseEntity<Void> deleteEvents(
            @ApiParam(value = "삭제할 모임 Id(seq)") @PathVariable Long eventsId
            , @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("url : {}"
                , request.getRequestURI()
        );

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        eventsService.deleteEvents(eventsId, accounts);
        return ResponseEntity.ok().build();

    }

    @ApiOperation(value = "모임 참여 관리", notes = "JWT 토큰 필수")
    @PostMapping("/{eventsId}/accounts")
    public ResponseEntity<AccountsEventsDto<EventsDto>> participateEventsManage(
            @ApiParam(value = "참여 관리할 모임 Id(seq)") @PathVariable Long eventsId
            , @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {

        log.info("url : {}"
                , request.getRequestURI()
        );

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        return ResponseEntity.ok(eventsService.participateEventsManage(eventsId, accounts));
    }

    /**
     * Events 초대
     */
//    @PostMapping("/{eventsId}/accounts/{accountsId}")
//    public ResponseEntity<AccountsEventsDto<EventsDto>> inviteEvents(
//            @PathVariable Long eventsId
//            , @PathVariable Long accountsId
//            , @CurrentAccount Accounts accounts
//            , HttpServletRequest request
//    ) {
//
//        log.info("url : {}"
//                , request.getRequestURI()
//        );
//
//        return ResponseEntity.ok(eventsService.inviteEvents(eventsId, accounts, accountsId));
//    }

    /**
     * Events 초대 응답
     */
//    @PutMapping("/{eventsId}/accounts")
//    public ResponseEntity<AccountsEventsDto<AccountsDto>> responseEventsInvite(
//            @PathVariable Long eventsId
//            , @CurrentAccount Accounts accounts
//            , @RequestBody @Valid UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq
//            , HttpServletRequest request
//    ) {
//        log.info("url : {}, data : {}"
//                , request.getRequestURI()
//                , updateAccountsMapperEventsReq
//        );
//
//        return ResponseEntity.ok(eventsService.responseEventsInvite(
//                eventsId
//                , accounts
//                , updateAccountsMapperEventsReq)
//        );
//    }
}
