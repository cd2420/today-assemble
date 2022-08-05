package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
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
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

    private final AccountsService accountsService;

    /**
     * 회원 리스트 조회
     */
    @GetMapping("/list")
    public ResponseEntity<List<AccountsDto>> getAccountList(
            HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<AccountsDto> accountList = accountsService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    /**
     * Jwt 로 회원 상세 조회
     */
    @GetMapping("")
    public ResponseEntity<AccountsDto> getAccountByJwt(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        AccountsDto accountsDto = accountsService.getAccount(accounts);
        return ResponseEntity.ok(accountsDto);
    }

    /**
     * 회원 id(seq)로 회원 조회
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountsDto> getAccountByPathVariable(
            @PathVariable Long accountId
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), accountId);
        AccountsDto accountsDto = accountsService.getAccount(accountId);
        return ResponseEntity.ok(accountsDto);
    }

    /**
     * 내가 좋아요 누른 모임 리스트 조회
     */
    @GetMapping("/likes/events")
    public ResponseEntity<List<EventsDto>> getAccountLikesEventList(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
            , @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<EventsDto> eventsDtoList = accountsService.getAccountLikesEventList(pageable, accounts);
        return ResponseEntity.ok(eventsDtoList);
    }

    /**
     * 좋아요 누른 모임 개수
     */
    @GetMapping("/likes/events/size")
    public ResponseEntity<Integer> getAccountLikesEventSize(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request

    ) {
        log.info("api : {}" , request.getRequestURI());
        Integer total = accountsService.getAccountLikesEventSize(accounts);
        return ResponseEntity.ok(total);
    }

    /**
     * 내가 참여한 모임 리스트 조회
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventsDto>> getAccountParticipateEvents(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
            , @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<EventsDto> eventsDtoList = accountsService.getAccountParticipateEvents(pageable, accounts);
        return ResponseEntity.ok(eventsDtoList);
    }

    /**
     * 내가 참여하 모임 리스트 개수
     */
    @GetMapping("/events/size")
    public ResponseEntity<Integer> getParticipateEventSize(
            @CurrentAccount Accounts accounts
    ) {
        return ResponseEntity.ok(accountsService.getParticipateEventSize(accounts));
    }

    /**
     * 내가 참여중인 모임인지 확인
     */
    @GetMapping("/events/{eventsId}")
    public ResponseEntity<Boolean> checkAccountParticipateEvents(
            @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        Boolean result = accountsService.checkAccountParticipateEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }

    /**
     * 내가 좋아요한 모임인지 확인
     */
    @GetMapping("/likes/{eventsId}")
    public ResponseEntity<Boolean> checkAccountLikesEvents(
            @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        Boolean result = accountsService.checkAccountLikesEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }
}
