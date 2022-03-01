package com.lim.assemble.todayassemble.email.controller;

import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String checkEmailToken(
            @RequestParam String token
            , @RequestParam String email
            , HttpServletRequest request
    ) {
        log.info("url: {}, token: {}, email: {}"
                , request.getRequestURI()
                , token
                , email
        );
        emailService.verify(email, token);
        return "redirect:" + appProperties.getFront_host();
    }

    @GetMapping("/login-email-token")
    public String loginEmailToken(
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
        if (!emailService.checkLoginToken(email, token, response)) {
            throw new TodayAssembleException(ErrorCode.WRONG_EMAIL_TOKEN);
        }
        return "redirect:" + appProperties.getFront_host();
    }

}
