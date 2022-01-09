package com.lim.assemble.todayassemble.email.service;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.common.type.EmailsType;

public interface EmailService {

    public void sendEmail(AccountsDto accountsDto, EmailsType emailsType);
}
