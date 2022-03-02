package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Profile("test")
@Service
@Slf4j
public class TestEmailServiceImpl implements EmailService {
    @Override
    public Object sendEmail(Accounts accounts, EmailsType emailsType) {
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info("sent emailData: {}, type: {}" , accounts, emailsType);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        return null;
    }

    @Override
    public AccountsDto emailToken(String email, String token, EmailsType emailsType, HttpServletResponse response) {
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info("emailToken email : {}, token: {}", email, token);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        return null;
    }


}
