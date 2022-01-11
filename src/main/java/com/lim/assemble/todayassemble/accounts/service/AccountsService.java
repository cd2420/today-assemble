package com.lim.assemble.todayassemble.accounts.service;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountsService {
    List<AccountsDto> getAccountList();

    AccountsDto getAccount(Long accountId);

    List<EventsDto> getAccountLikesEventList(Long accountId);

    AccountsDto signUp(CreateAccountReq createAccountReq);

    void accountLikesEvent(Long eventId, AccountsDto accountsDto);

    AccountsDto updateAccount(Long accountId, EditAccountsReq editAccountsReq);

    void responseInvite(Long accountId, Long eventId, Boolean response);

    void passwordEncode(AccountReq accountReq);
}
