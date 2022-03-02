package com.lim.assemble.todayassemble.email.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final AppProperties appProperties;

    @GetMapping("/check-email-token")
    public ResponseEntity<AccountsDto> checkEmailToken(
            @RequestParam String token
            , @RequestParam String email
            , HttpServletRequest request
            , HttpServletResponse response
    ) {
        log.info("url: {}, token: {}, email: {}"
                , request.getRequestURI()
                , token
                , email
        );

        return ResponseEntity.ok(emailService.emailToken(email, token, EmailsType.VERIFY, response));
    }

    @GetMapping("/login-email-token")
    public ResponseEntity<AccountsDto> loginEmailToken(
            @RequestParam String token
            , @RequestParam String email
            , HttpServletRequest request
            , HttpServletResponse response
    ) {
        log.info("url: {}, token: {}, email: {}"
                , request.getRequestURI()
                , token
                , email
        );

        return ResponseEntity.ok(emailService.emailToken(email, token, EmailsType.LOGIN, response));
    }

}
