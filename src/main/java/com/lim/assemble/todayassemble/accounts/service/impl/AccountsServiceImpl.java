package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountReq;
import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.EditAccountsReq;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.likes.repository.LikesRepository;
import com.lim.assemble.todayassemble.likes.service.LikesService;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository accountsRepository;

    private final ValidationFactory validationFactory;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final LikesService likesService;

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
        // createAccountReq validation check: {이메일 중복체크}
        validationFactory
                .createValidation(ValidateType.SIGNUP)
                .validate(createAccountReq);

        // accounts 저장
        Accounts accounts = Accounts.from(createAccountReq).generateEmailCheckToken();

        // email 발송
        Email email = (Email) emailService.sendEmail(accounts, EmailsType.SIGNUP);
        accounts.setEmailSet(new HashSet<>());
        accounts.getEmailSet().add(email);

        AccountsDto accountsDto
                = AccountsDto.from(accountsRepository.save(accounts));

        return accountsDto;
    }

    @Override
    @Transactional
    public void manageAccountLikesEvent(Long eventId, Accounts accounts) {

        // 영속성 관리에 등록하기 위해 accounts를 이렇게 한번더 호출.
        accounts = getAccountsFromRepositoryByAccountId(accounts.getId());

        likesService.manageLikes(eventId, accounts);

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
