package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsCUController {

    private final AccountsService accountsService;

    @PostMapping("/sign-up")
    public ResponseEntity<AccountsDto> signUp(
            @RequestBody @Valid CreateAccountReq createAccountReq
            , HttpServletRequest request
    ) {
        accountsService.passwordEncode(createAccountReq);
        log.info("api : {}, data : {}" , request.getRequestURI(), createAccountReq);
        return ResponseEntity.ok(accountsService.signUp(createAccountReq));
    }

    @PostMapping("/likes/events/{eventId}")
    public ResponseEntity<EventsDto> manageAccountLikesEvent(
            @PathVariable Long eventId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, eventId: {}" , request.getRequestURI(), eventId);
        return ResponseEntity.ok(accountsService.manageAccountLikesEvent(eventId, accounts));
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountsDto> updateAccount(
            @PathVariable Long accountId
            , @RequestBody @Valid UpdateAccountsReq updateAccountsReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}"
                , request.getRequestURI()
                , accountId
        );

        return ResponseEntity.ok(accountsService.updateAccount(accountId, accounts, updateAccountsReq));
    }

    @PutMapping("/{accountId}/image")
    public ResponseEntity<AccountsDto> updateAccountImage(
            @PathVariable Long accountId
            , @RequestBody @Valid UpdateAccountsImageReq updateAccountsImageReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}"
                , request.getRequestURI()
                , accountId
        );

        return ResponseEntity.ok(accountsService.updateAccount(accountId, accounts, updateAccountsImageReq));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long accountId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}"
                , request.getRequestURI()
                , accountId
        );
        accountsService.deleteAccount(accountId, accounts);
        return ResponseEntity.ok().build();
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
