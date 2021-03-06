package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
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
     * @param request
     * @return
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
     * @param accounts
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseEntity<AccountsDto> getAccountByJwt(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        if (accounts == null ) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }
        AccountsDto accountsDto = AccountsDto.from(accounts);
        return ResponseEntity.ok(accountsDto);
    }

    /**
     * 회원 id(seq)로 회원 조회
     * @param accountId
     * @param request
     * @return
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
     * @param accounts
     * @param request
     * @param pageable
     * @return
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
     * @param accounts
     * @param request
     * @return
     */
    @GetMapping("/likes/events/size")
    public ResponseEntity<Integer> getAccountLikesEventSize(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request

    ) {
        log.info("api : {}" , request.getRequestURI());
        Integer total = accountsService.getAccountLikesEventSize(accounts.getId());
        return ResponseEntity.ok(total);
    }

    /**
     * 내가 참여한 모임 리스트 조회
     * @param accounts
     * @param request
     * @param pageable
     * @return
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
     * @param accounts
     * @return
     */
    @GetMapping("/events/size")
    public ResponseEntity<Integer> getParticipateEventSize(
            @CurrentAccount Accounts accounts
    ) {
        return ResponseEntity.ok(accountsService.getParticipateEventSize(accounts.getId()));
    }

    /**
     * 내가 참여중인 모임인지 확인
     * @param accounts
     * @param eventsId
     * @param request
     * @return
     */
    @GetMapping("/events/{eventsId}")
    public ResponseEntity<Boolean> checkAccountParticipateEvents(
            @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());

        boolean result = false;
        if (accounts.getAccountsEventsSet() != null) {
            result = accounts.getAccountsEventsSet().stream()
                    .anyMatch(item -> item.getEvents().getId().equals(eventsId));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 내가 좋아요한 모임인지 확인
     * @param accounts
     * @param eventsId
     * @param request
     * @return
     */
    @GetMapping("/likes/{eventsId}")
    public ResponseEntity<Boolean> checkAccountLikesEvents(
            @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());

        boolean result = false;
        if (accounts.getAccountsEventsSet() != null) {
            result = accounts.getLikesSet().stream()
                    .anyMatch(item -> item.getEvents().getId().equals(eventsId));
        }
        return ResponseEntity.ok(result);
    }
}
