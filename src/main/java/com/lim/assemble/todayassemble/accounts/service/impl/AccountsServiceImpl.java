package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsImages;
import com.lim.assemble.todayassemble.accounts.projection.LoadUserByUsernameAccounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsEventsRepository;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.service.CommonService;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.likes.repository.LikesRepository;
import com.lim.assemble.todayassemble.likes.service.LikesService;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private final AccountsRepository accountsRepository;
    private final AccountsEventsRepository accountsEventsRepository;
    private final LikesRepository likesRepository;

    private final ValidationFactory validationFactory;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final LikesService likesService;
    private final CommonService commonService;


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
    public List<EventsDto> getAccountLikesEventList(Pageable pageable, Accounts accounts) {
        accounts = getAccountsFromRepositoryByAccountId(accounts.getId());
        List<EventsDto> accountLikesEventList = likesService.getAccountLikesEventList(accounts)
                                                            .stream().sorted(Comparator.comparing(EventsDto::getEventsTime))
                                                            .collect(Collectors.toList());
        final PageImpl<EventsDto> page = getPageImpl(pageable, accountLikesEventList);

        return page.stream().collect(Collectors.toList());
    }

    private Accounts getAccountsFromRepositoryByAccountId(Long accountId) {
        return accountsRepository.findById(accountId)
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> getAccountParticipateEvents(Pageable pageable, Accounts accounts) {

        List<Events> eventsList = accountsEventsRepository.findByAccountsId(accounts.getId())
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT))
                .stream()
                .map(item -> item.getEvents())
                .sorted(Comparator.comparing(Events::getEventsTime))
                .collect(Collectors.toList());

        final PageImpl<Events> page = getPageImpl(pageable, eventsList);

        return page.stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAccountLikesEventSize(Long id) {

        return likesRepository.findByAccountsId(id)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT))
                .size();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getParticipateEventSize(Long id) {

        return accountsEventsRepository.findByAccountsId(id)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT))
                .size();
    }

    private <T> PageImpl<T> getPageImpl(Pageable pageable, List<T> list) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        final PageImpl<T> page = new PageImpl<>(list.subList(start, end), pageable, list.size());
        return page;
    }

    @Override
    @Transactional
    public AccountsDto signUp(CreateAccountReq createAccountReq, HttpServletResponse response) {
        // createAccountReq validation check: {????????? ????????????}
        validationFactory
                .createValidation(ValidateType.ACCOUNT)
                .validate(ValidateSituationType.CREATE, createAccountReq);

        // accounts ??????
        Accounts accounts = Accounts.from(createAccountReq).generateEmailCheckToken();

        // email ??????
        Email email = (Email) emailService.sendEmail(accounts, EmailsType.VERIFY);
        accounts.setEmailSet(new HashSet<>());
        accounts.getEmailSet().add(email);

        AccountsDto accountsDto
                = AccountsDto.from(accountsRepository.save(accounts));

        commonService.loginWithToken(response, accounts);
        return accountsDto;
    }

    @Override
    @Transactional
    public EventsDto manageAccountLikesEvent(Long eventId, Accounts accounts) {

        return likesService.manageLikes(eventId, accounts);
    }

    @Override
    @Transactional
    public AccountsDto updateAccount(Accounts accounts, UpdateAccountsReq updateAccountsReq) {

        accounts = accountsRepository.findById(accounts.getId()).orElseThrow(
                () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT)
        );

        accounts.setName(updateAccountsReq.getName());
        accounts.setGender(updateAccountsReq.getGender());
        accounts.setBirth(updateAccountsReq.getBirth());
        accounts.setAge(updateAccountsReq.getAge());

        if (updateAccountsReq.getAccountsImagesDto() != null) {
            updateAccountsImage(accounts, updateAccountsReq.getAccountsImagesDto());
        }

        if(updateAccountsReq.getPassword() != null && !updateAccountsReq.getPassword().isEmpty()) {
            updateAccountPassword(accounts, updateAccountsReq);
        }

        return AccountsDto.from(accounts);
    }

    public void updateAccountsImage(Accounts accounts, AccountsImagesDto accountsImagesDto) {
        AccountsImages accountsImages = accounts.getAccountsImages();

        if (accountsImages != null) {
            accountsImages.setImage(accountsImagesDto.getImage());
        } else {
            accounts.setAccountsImages(
                    AccountsImages.builder()
                    .accounts(accounts)
                    .imagesType(ImagesType.MAIN)
                    .image(accountsImagesDto.getImage())
                    .build()
            );
        }
    }

    public void updateAccountPassword(Accounts accounts, UpdateAccountsReq updateAccountsReq) {
        String updatePassword = updateAccountsReq.getPassword();

        if (!passwordEncoder.matches(updatePassword, accounts.getPassword())) {
            accounts.setPassword(passwordEncoder.encode(updatePassword));
        }
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

    @Override
    @Transactional
    public void deleteAccount(Long accountId, Accounts accounts) {
        // delete ???????????? ????????? ????????? ????????? ??????.
        validationFactory
                .createValidation(ValidateType.ACCOUNT)
                .validate(ValidateSituationType.UPDATE, new UpdateAccountsDto(accountId, accounts.getId()));

        accountsRepository.delete(accounts);

    }

    @Override
    @Transactional
    public void login(AccountsCredentials accountsCredentials) {
        Accounts accounts = getAccountsByEmail(accountsCredentials.getEmail());
        accounts.generateLoginEmailToken();
        // email ??????
        Email email = (Email) emailService.sendEmail(accounts, EmailsType.LOGIN);
        accounts.getEmailSet().add(email);
    }

    private Accounts getAccountsByEmail(String email) {
        return accountsRepository.findByEmail(email, Accounts.class)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoadUserByUsernameAccounts loadUserByUsernameAccounts = accountsRepository.findByEmail(email, LoadUserByUsernameAccounts.class)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));
        Accounts accounts = Accounts.builder()
                .email(loadUserByUsernameAccounts.getEmail())
                .password(loadUserByUsernameAccounts.getPassword())
                .emailVerified(loadUserByUsernameAccounts.getEmailVerified())
                .accountType(loadUserByUsernameAccounts.getAccountType())
                .name(loadUserByUsernameAccounts.getName())
                .gender(loadUserByUsernameAccounts.getGender())
                .birth(loadUserByUsernameAccounts.getBirth())
                .age(loadUserByUsernameAccounts.getAge())
                .build();
        accounts.setId(loadUserByUsernameAccounts.getId());
        return new UserAccount(accounts);
    }
}
