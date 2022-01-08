package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.EditAccountsReq;
import com.lim.assemble.todayassemble.accounts.dto.LoginAccountReq;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsCUController {

    private final AccountsService accountsService;

    @PostMapping("/sign-up")
    public ResponseEntity<AccountsDto> signUp(
            @RequestBody CreateAccountReq createAccountReq
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), createAccountReq);
        AccountsDto accountsDto = accountsService.signUp(createAccountReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountsDto> logIn(
            @RequestBody LoginAccountReq loginAccountReq
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), loginAccountReq);
        AccountsDto accountsDto = accountsService.logIn(loginAccountReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PostMapping("/{eventId}/events")
    public ResponseEntity<Void> accountLikesEvent(
            @PathVariable Long eventId
            , @RequestBody AccountsDto accountsDto
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}, eventId: {}" , request.getRequestURI(), accountsDto, eventId);
        accountsService.accountLikesEvent(eventId, accountsDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountsDto> updateAccount(
            @PathVariable Long accountId
            , @RequestBody EditAccountsReq editAccountsReq
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}, accountId : {}"
                , request.getRequestURI()
                , editAccountsReq
                , accountId
        );
        AccountsDto accountsDto = accountsService.updateAccount(accountId, editAccountsReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PutMapping("/{accountId}/events/{eventId}")
    public ResponseEntity<Void> responseInvite(
            @PathVariable Long accountId
            , @PathVariable Long eventId
            , @RequestParam("response") Boolean response
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}, eventId : {}, response ; {} "
                , request.getRequestURI()
                , accountId
                , eventId
                , response
        );
        accountsService.responseInvite(
                accountId
                , eventId
                , response
        );
        return ResponseEntity.ok().build();
    }

}
