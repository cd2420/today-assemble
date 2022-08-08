package com.lim.assemble.todayassemble.email.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "계정 인증 토큰 체크")
    @GetMapping("/check-email-token")
    public ResponseEntity<AccountsDto> checkEmailToken(
            @ApiParam(value = "토큰") @RequestParam String token
            , @ApiParam(value = "email")  @RequestParam String email
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

    @ApiOperation(value = "이메일 로그인 토큰 체크")
    @GetMapping("/login-email-token")
    public ResponseEntity<AccountsDto> loginEmailToken(
            @ApiParam(value = "토큰") @RequestParam String token
            , @ApiParam(value = "email")  @RequestParam String email
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
