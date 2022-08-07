package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsCredentials;
import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CurrentAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            , @ApiParam(value = "리턴받을 객체에 이미지 넣을지 말지 결정") @RequestParam Boolean images
            , HttpServletRequest request
    ) {
        log.info("api : {}, images: {}" , request.getRequestURI(), images);

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        AccountsDto accountsDto = accountsService.getAccount(accounts, images);
        return ResponseEntity.ok(accountsDto);
    }

    @ApiOperation(value = "회원 id(seq)로 조회")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountsDto> getAccountByPathVariable(
            @ApiParam(value = "조회할 회원 ID(seq)") @PathVariable Long accountId
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

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

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

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

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

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        List<EventsDto> eventsDtoList = accountsService.getAccountParticipateEvents(pageable, accounts);
        return ResponseEntity.ok(eventsDtoList);
    }

    @ApiOperation(value = "내가 참여한 모임 리스트 개수 조회")
    @GetMapping("/events/size")
    public ResponseEntity<Integer> getParticipateEventSize(
            @ApiIgnore @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        return ResponseEntity.ok(accountsService.getParticipateEventSize(accounts));
    }

    @ApiOperation(value = "내가 참여중인 모임인지 체크")
    @GetMapping("/events/{eventsId}")
    public ResponseEntity<Boolean> checkAccountParticipateEvents(
            @ApiIgnore @CurrentAccount Accounts accounts
            , @ApiParam(value = "참여중인 모임 id") @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        Boolean result = accountsService.checkAccountParticipateEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "내가 '좋아요' 누른 모임인지 체크")
    @GetMapping("/likes/{eventsId}")
    public ResponseEntity<Boolean> checkAccountLikesEvents(
            @ApiIgnore @CurrentAccount Accounts accounts
            , @ApiParam(value = "'좋아요' 누른 모임 id") @PathVariable Long eventsId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());

        if (accounts == null) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }

        Boolean result = accountsService.checkAccountLikesEvents(accounts, eventsId);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "JWT 값 받아오기 - (오직 swagger 용)")
    @GetMapping("/jwt")
    public ResponseEntity<String> getJwt(

            @ApiParam(value = "jwt 받기위한 회원 인증") AccountsCredentials accountsCredentials
            , HttpServletResponse response
    ) {
        String jwt = accountsService.getJWT(accountsCredentials, response);
        return ResponseEntity.ok(jwt);
    }
}
