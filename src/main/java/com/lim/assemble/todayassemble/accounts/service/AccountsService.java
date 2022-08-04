package com.lim.assemble.todayassemble.accounts.service;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface AccountsService extends UserDetailsService {
    List<AccountsDto> getAccountList();

    AccountsDto getAccount(Long accountId);

    AccountsDto getAccount(Accounts accounts);

    List<EventsDto> getAccountLikesEventList(Pageable pageable, Accounts accounts);

    AccountsDto signUp(CreateAccountReq createAccountReq, HttpServletResponse response);

    EventsDto manageAccountLikesEvent(Long eventId, Accounts accounts);

    AccountsDto updateAccount(Accounts accounts, UpdateAccountsReq updateAccountsReq);

    void responseInvite(Long accountId, Long eventId, Boolean response);

    void passwordEncode(AccountReq accountReq);

    void deleteAccount(Long accountId, Accounts accounts);

    void login(AccountsCredentials accountsCredentials);

    List<EventsDto> getAccountParticipateEvents(Pageable pageable, Accounts accounts);

    Integer getAccountLikesEventSize(Accounts accounts);

    Integer getParticipateEventSize(Accounts accounts);

    Boolean checkAccountParticipateEvents(Accounts accounts, Long eventsId);

    Boolean checkAccountLikesEvents(Accounts accounts, Long eventsId);
}
