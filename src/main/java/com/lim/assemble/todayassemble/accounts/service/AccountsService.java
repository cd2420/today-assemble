package com.lim.assemble.todayassemble.accounts.service;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;

import java.util.List;

public interface AccountsService {
    List<AccountsDto> getAccountList();

    AccountsDto getAccount(Long accountId);

    List<EventsDto> getAccountLikesEventList(Long accountId);

    AccountsDto signUp(CreateAccountReq createAccountReq);

    void manageAccountLikesEvent(Long eventId, Accounts accounts);

    AccountsDto updateAccount(Long accountId, EditAccountsReq editAccountsReq);

    void responseInvite(Long accountId, Long eventId, Boolean response);

    void passwordEncode(AccountReq accountReq);
}
