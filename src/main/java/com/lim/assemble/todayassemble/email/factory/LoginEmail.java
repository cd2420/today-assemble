package com.lim.assemble.todayassemble.email.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import org.springframework.stereotype.Component;

@Component
public class LoginEmail implements EmailForm {

    private final String linkName = "이메일 로그인하기";
    private final String payLoad = "오늘의 모임 로그인 하시려면 링크를 클릭하세요.";
    private final String subject = "오늘의 모임, 이메일로 로그인";
    private final String type = "email-login";
    private String token = "";

    @Override
    public void setToken(Accounts accounts) {
        this.token = accounts.getEmailLoginToken();
    }

    @Override
    public EmailsType getEmailType() {
        return EmailsType.LOGIN;
    }

    @Override
    public String getPayLoad() {
        return payLoad;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLinkName() {
        return linkName;
    }
}
