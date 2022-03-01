package com.lim.assemble.todayassemble.accounts.service;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AccountsService {
    List<AccountsDto> getAccountList();

    AccountsDto getAccount(Long accountId);

    List<EventsDto> getAccountLikesEventList(Accounts accounts);

    AccountsDto signUp(CreateAccountReq createAccountReq, HttpServletResponse response);

    EventsDto manageAccountLikesEvent(Long eventId, Accounts accounts);

    AccountsDto updateAccount(Long accountId, Accounts accounts, UpdateAccountsReq updateAccountsReq);

    void responseInvite(Long accountId, Long eventId, Boolean response);

    void passwordEncode(AccountReq accountReq);

    void deleteAccount(Long accountId, Accounts accounts);
}
