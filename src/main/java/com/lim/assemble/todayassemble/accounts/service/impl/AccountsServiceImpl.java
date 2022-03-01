package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsImages;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.config.AuthenticationService;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.likes.service.LikesService;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    public List<EventsDto> getAccountLikesEventList(Accounts accounts) {
        accounts = getAccountsFromRepositoryByAccountId(accounts.getId());
        return likesService.getAccountLikesEventList(accounts);
    }

    private Accounts getAccountsFromRepositoryByAccountId(Long accountId) {
        return accountsRepository.findById(accountId)
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT)
                );
    }

    @Override
    @Transactional
    public AccountsDto signUp(CreateAccountReq createAccountReq, HttpServletResponse response) {
        // createAccountReq validation check: {이메일 중복체크}
        validationFactory
                .createValidation(ValidateType.ACCOUNT)
                .validate(ValidateSituationType.CREATE, createAccountReq);

        // accounts 저장
        Accounts accounts = Accounts.from(createAccountReq).generateEmailCheckToken();

        // email 발송
        Email email = (Email) emailService.sendEmail(accounts, EmailsType.SIGNUP);
        accounts.setEmailSet(new HashSet<>());
        accounts.getEmailSet().add(email);

        AccountsDto accountsDto
                = AccountsDto.from(accountsRepository.save(accounts));
        AuthenticationService.addJWTToken(response, accountsDto.getEmail());
        return accountsDto;
    }

    @Override
    @Transactional
    public EventsDto manageAccountLikesEvent(Long eventId, Accounts accounts) {

        // 영속성 관리에 등록하기 위해 accounts를 이렇게 한번더 호출.
        accounts = getAccountsFromRepositoryByAccountId(accounts.getId());

        return likesService.manageLikes(eventId, accounts);
    }

    @Override
    @Transactional
    public AccountsDto updateAccount(Long accountId, Accounts accounts, UpdateAccountsReq updateAccountsReq) {
        // update 시킬려는 대상이 본인이 맞는지 확인.
        validationFactory
                .createValidation(ValidateType.ACCOUNT)
                .validate(ValidateSituationType.UPDATE, new UpdateAccountsDto(accountId, accounts.getId()));

        accounts = getAccountsFromRepositoryByAccountId(accountId);

        if (UpdateAccountsReq.class.equals(updateAccountsReq.getClass())) {
            updateAccountBasic(accounts, updateAccountsReq);
        } else {
            updateAccountImage(accounts, (UpdateAccountsImageReq) updateAccountsReq);
        }

        return AccountsDto.from(accounts);
    }

    public void updateAccountBasic(Accounts accounts, UpdateAccountsReq updateAccountsReq) {
        String updatePassword = updateAccountsReq.getPassword();

        if (!passwordEncoder.matches(updatePassword, accounts.getPassword())) {
            updateAccountsReq.setPassword(passwordEncoder.encode(updatePassword));
        }
        accounts.update(updateAccountsReq);

    }

    public void updateAccountImage(Accounts accounts, UpdateAccountsImageReq updateAccountsImageReq) {

        AccountsImages accountsImages = accounts.getAccountsImages();
        AccountsImagesDto accountsImagesDto = updateAccountsImageReq.getAccountsImagesDto();

        if (accountsImages != null) {
            accountsImages.setImage(accountsImagesDto.getImage());
        } else {

            accounts.setAccountsImages(AccountsImages.builder()
                    .accounts(accounts)
                    .imagesType(ImagesType.MAIN)
                    .image(accountsImagesDto.getImage())
                    .build()
            );
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
        // delete 시킬려는 대상이 본인이 맞는지 확인.
        validationFactory
                .createValidation(ValidateType.ACCOUNT)
                .validate(ValidateSituationType.UPDATE, new UpdateAccountsDto(accountId, accounts.getId()));

        accountsRepository.delete(accounts);

    }

}
