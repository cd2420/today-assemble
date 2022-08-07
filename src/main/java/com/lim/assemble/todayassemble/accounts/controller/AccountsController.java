package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

    private final AccountsService accountsService;

    @ApiOperation(value = "회원 리스트 조회")
    @GetMapping("/list")
    public ResponseEntity<List<AccountsDto>> getAccountList(
            HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<AccountsDto> accountList = accountsService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    @ApiOperation(value = "JWT로 회원 상세 조회")
    @GetMapping("")
    public ResponseEntity<AccountsDto> getAccountByJwt(
            @ApiIgnore @CurrentAccount Accounts accounts
            , @RequestParam Boolean images
            , HttpServletRequest request
    ) {
        log.info("api : {}, images: {}" , request.getRequestURI(), images);
        AccountsDto accountsDto = accountsService.getAccount(accounts, images);
        return ResponseEntity.ok(accountsDto);
    }

    @ApiOperation(value = "회원 id(seq)로 조회")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountsDto> getAccountByPathVariable(
            @PathVariable Long accountId
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), accountId);
        AccountsDto accountsDto = accountsService.getAccount(accountId);
        return ResponseEntity.ok(accountsDto);
    }

    @ApiOperation(value = "좋아요 누른 모임 리스트 조회")
    @GetMapping("/likes/events")
    public ResponseEntity<List<EventsDto>> getAccountLikesEventList(
            @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
            , @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<EventsDto> eventsDtoList = accountsService.getAccountLikesEventList(pageable, accounts);
        return ResponseEntity.ok(eventsDtoList);
    }

    @ApiOperation(value = "좋아요 누른 모임 개수 조회")
    @GetMapping("/likes/events/size")
    public ResponseEntity<Integer> getAccountLikesEventSize(
            @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request

    ) {
        log.info("api : {}" , request.getRequestURI());
        Integer total = accountsService.getAccountLikesEventSize(accounts);
        return ResponseEntity.ok(total);
    }

    @ApiOperation(value = "내가 참여한 모임 리스트 조회")
    @GetMapping("/events")
    public ResponseEntity<List<EventsDto>> getAccountParticipateEvents(
            @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
            , @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<EventsDto> eventsDtoList = accountsService.getAccountParticipateEvents(pageable, accounts);
        return ResponseEntity.ok(eventsDtoList);
    }

    @ApiOperation(value = "내가 참여한 모임 리스트 개수 조회")
    @GetMapping("/events/size")
    public ResponseEntity<Integer> getParticipateEventSize(
            @ApiIgnore @CurrentAccount Accounts accounts
    ) {
        return ResponseEntity.ok(accountsService.getParticipateEventSize(accounts));
    }

    @ApiOperation(value = "내가 참여중인 모임인지 체크")
    @GetMapping("/events/{eventsId}")
    public ResponseEntity<Boolean> checkAccountParticipateEvents(
            @ApiIgnore @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        Boolean result = accountsService.checkAccountParticipateEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내가 '좋아요' 누른 모임인지 체크")
    @GetMapping("/likes/{eventsId}")
    public ResponseEntity<Boolean> checkAccountLikesEvents(
            @ApiIgnore @CurrentAccount Accounts accounts
            , @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        Boolean result = accountsService.checkAccountLikesEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }
}
