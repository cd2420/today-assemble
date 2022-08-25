package com.lim.assemble.todayassemble.email.factory;

import com.lim.assemble.todayassemble.common.type.EmailsType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class EmailFactory {

    private final HashMap<EmailsType, EmailForm> emailMap = new HashMap<>();


    private EmailFactory(List<EmailForm> emailTypes) {
        for(EmailForm emailType: emailTypes) {
            emailMap.put(emailType.getEmailType(), emailType);
        }
    }

    public EmailForm createValidation(EmailsType emailType) {
        if (EmailsType.LOGIN == emailType) {
            return emailMap.get(EmailsType.LOGIN);
        } else {
            return emailMap.get(EmailsType.VERIFY);
        }

    }


}
