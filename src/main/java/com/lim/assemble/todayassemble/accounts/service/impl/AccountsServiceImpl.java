package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public List<AccountsDto> getAccountList() {
        return Optional.of(accountsRepository.findAll())
                .orElse(new ArrayList<>())
                .stream()
                .map(AccountsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountsDto getAccount(Long accountId) {
        Accounts accounts = getAccountsFromRepositoryByAccountId(accountId);
        return AccountsDto.from(accounts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> getAccountLikesEventList(Long accountId) {
        Accounts accounts = getAccountsFromRepositoryByAccountId(accountId);

        return accounts.getLikesSet()
                .stream()
                .map(like -> EventsDto.from(like.getEvents()))
                .collect(Collectors.toList());
    }

    private Accounts getAccountsFromRepositoryByAccountId(Long accountId) {
        return accountsRepository.findById(accountId)
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT)
                );
    }

    @Override
    @Transactional
    public AccountsDto signUp(CreateAccountReq createAccountReq) {
        return null;
    }

    @Override
    @Transactional
    public AccountsDto logIn(LoginAccountReq loginAccountReq) {
        return null;
    }

    @Override
    @Transactional
    public void accountLikesEvent(Long eventId, AccountsDto accountsDto) {

    }

    @Override
    @Transactional
    public AccountsDto updateAccount(Long accountId, EditAccountsReq editAccountsReq) {
        return null;
    }

    @Override
    @Transactional
    public void responseInvite(Long accountId, Long eventId, Boolean response) {

    }

    @Override
    public void passwordEncode(AccountReq accountReq) {
        accountReq.setPassword(
                passwordEncoder.encode(accountReq.getPassword())
        );

    }
}
