package com.lim.assemble.todayassemble.email.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;

public interface EmailService {

    public void sendEmail(Accounts accounts, EmailsType emailsType);

    void verify(String email, String token);
}
