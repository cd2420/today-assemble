package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
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
    public void sendEmail(AccountsDto accountsDto, EmailsType emailsType) {
        log.info("sent email: {}, type: {}" , accountsDto, emailsType);
    }
}
