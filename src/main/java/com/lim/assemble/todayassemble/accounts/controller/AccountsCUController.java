package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.EditAccountsReq;
import com.lim.assemble.todayassemble.accounts.dto.LoginAccountReq;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountsCUController {

    private final AccountsService accountsService;

    @PostMapping("/sign-up")
    public ResponseEntity<AccountsDto> signUp(
            CreateAccountReq createAccountReq
    ) {
        AccountsDto accountsDto = accountsService.signUp(createAccountReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AccountsDto> logIn(
            LoginAccountReq loginAccountReq
    ) {
        AccountsDto accountsDto = accountsService.logIn(loginAccountReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PostMapping("/{eventId}/events")
    public ResponseEntity<Void> accountLikesEvent(
            @PathVariable Long eventId
            , AccountsDto accountsDto
    ) {
        accountsService.accountLikesEvent(eventId, accountsDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<AccountsDto> updateAccount(
            @PathVariable Long accountId
            , EditAccountsReq editAccountsReq
    ) {
        AccountsDto accountsDto = accountsService.updateAccount(accountId, editAccountsReq);
        return ResponseEntity.ok(accountsDto);
    }

    @PutMapping("/{accountId}/events/{eventId}")
    public ResponseEntity<Void> responseInvite(
            @PathVariable Long accountId
            , @PathVariable Long eventId
            , @RequestParam("response") Boolean response
    ) {
        accountsService.responseInvite(
                accountId
                , eventId
                , response
        );
        return ResponseEntity.ok().build();
    }

}
