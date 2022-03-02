package com.lim.assemble.todayassemble.email.service;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;

import javax.servlet.http.HttpServletResponse;

public interface EmailService {

    public Object sendEmail(Accounts accounts, EmailsType emailsType);

    AccountsDto emailToken(String email, String token, EmailsType emailsType, HttpServletResponse response);

}
