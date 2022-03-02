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

    @GetMapping("/list")
    public ResponseEntity<List<AccountsDto>> getAccountList(
            HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<AccountsDto> accountList = accountsService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("")
    public ResponseEntity<AccountsDto> getAccountByJwt(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        if (accounts == null ) {
            throw new TodayAssembleException(ErrorCode.NO_ACCOUNT);
        }
        AccountsDto accountsDto = accountsService.getAccount(accounts.getId());
        return ResponseEntity.ok(accountsDto);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountsDto> getAccountByPathVariable(
            @PathVariable Long accountId
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), accountId);
        AccountsDto accountsDto = accountsService.getAccount(accountId);
        return ResponseEntity.ok(accountsDto);
    }

    @GetMapping("/likes/events")
    public ResponseEntity<List<EventsDto>> getAccountLikesEventList(
            @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        List<EventsDto> eventsDtoList = accountsService.getAccountLikesEventList(accounts);
        return ResponseEntity.ok(eventsDtoList);
    }


}
