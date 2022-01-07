package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    @GetMapping("")
    public ResponseEntity<List<AccountsDto>> getAccountList() {
        List<AccountsDto> accountList = accountsService.getAccountList();
        return ResponseEntity.ok(accountList);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountsDto> getAccount(
            @PathVariable Long accountId
    ) {
        AccountsDto accountsDto = accountsService.getAccount(accountId);
        return ResponseEntity.ok(accountsDto);
    }

    @GetMapping("/{accountId}/likes/events")
    public ResponseEntity<List<EventsDto>> getAccountLikesEventList(
            @PathVariable Long accountId
    ) {
        List<EventsDto> eventsDtoList = accountsService.getAccountLikesEventList(accountId);
        return ResponseEntity.ok(eventsDtoList);
    }


}
