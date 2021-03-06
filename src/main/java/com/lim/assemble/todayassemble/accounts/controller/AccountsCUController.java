package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountsCUController {

    private final AccountsService accountsService;

    /**
     * 패스워드 없이 로그인
     * 1. email로 로그인 token 발급
     * 2. 로그인 token 으로 로그인
     * @param accountsCredentials
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody AccountsCredentials accountsCredentials
            , HttpServletRequest request
    ) {
        log.info("api : {}, data : {}" , request.getRequestURI(), accountsCredentials);
        accountsService.login(accountsCredentials);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원가입
     * @param createAccountReq
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<AccountsDto> signUp(
            @RequestBody @Valid CreateAccountReq createAccountReq
            , HttpServletRequest request
            , HttpServletResponse response
    ) {
        accountsService.passwordEncode(createAccountReq);
        log.info("api : {}, data : {}" , request.getRequestURI(), createAccountReq);
        return ResponseEntity.ok(accountsService.signUp(createAccountReq, response));
    }

    /**
     * 좋아요 관리
     * @param eventId
     * @param accounts
     * @param request
     * @return
     */
    @PostMapping("/likes/events/{eventId}")
    public ResponseEntity<EventsDto> manageAccountLikesEvent(
            @PathVariable Long eventId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, eventId: {}" , request.getRequestURI(), eventId);
        return ResponseEntity.ok(accountsService.manageAccountLikesEvent(eventId, accounts));
    }

    /**
     * 회원정보 수정
     * @param accountId
     * @param updateAccountsReq
     * @param accounts
     * @param request
     * @return
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountsDto> updateAccount(
            @PathVariable Long accountId
            , @RequestBody @Valid UpdateAccountsReq updateAccountsReq
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}"
                , request.getRequestURI()
                , accountId
        );

        return ResponseEntity.ok(accountsService.updateAccount(accountId, accounts, updateAccountsReq));
    }

    /**
     * 회원 탈퇴
     * @param accountId
     * @param accounts
     * @param request
     * @return
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long accountId
            , @CurrentAccount Accounts accounts
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}"
                , request.getRequestURI()
                , accountId
        );
        accountsService.deleteAccount(accountId, accounts);
        return ResponseEntity.ok().build();
    }

    /**
     * 초대 응답
     * @param accountId
     * @param eventId
     * @param response
     * @param request
     * @return
     */
    @PutMapping("/{accountId}/events/{eventId}")
    public ResponseEntity<Void> responseInvite(
            @PathVariable Long accountId
            , @PathVariable Long eventId
            , @RequestParam("response") Boolean response
            , HttpServletRequest request
    ) {
        log.info("api : {}, accountId : {}, eventId : {}, response ; {} "
                , request.getRequestURI()
                , accountId
                , eventId
                , response
        );
        accountsService.responseInvite(
                accountId
                , eventId
                , response
        );
        return ResponseEntity.ok().build();
    }

}
