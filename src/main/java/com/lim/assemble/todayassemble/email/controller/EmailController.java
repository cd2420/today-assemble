package com.lim.assemble.todayassemble.email.controller;

import com.lim.assemble.todayassemble.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/check-email-token")
    public ResponseEntity<Void> checkEmailToken(
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
        return ResponseEntity.ok().build();
    }

}
