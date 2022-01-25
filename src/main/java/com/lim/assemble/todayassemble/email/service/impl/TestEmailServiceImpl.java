package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
@Slf4j
public class TestEmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(Accounts accounts, EmailsType emailsType) {
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info("sent emailData: {}, type: {}" , accounts, emailsType);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

    @Override
    public void verify(String email, String token) {
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
        log.info("verify true email : {}, token: {}", email, token);
        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$");
    }
}
