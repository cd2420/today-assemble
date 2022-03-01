package com.lim.assemble.todayassemble.email.controller;

import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

}
