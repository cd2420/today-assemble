package com.lim.assemble.todayassemble.email.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import org.springframework.stereotype.Component;

@Component
public class VerifyEmail implements EmailForm {

    private final String linkName = "이메일 인증하기";
    private final String payLoad = "오늘의 모임 서비스를 사용하려면 링크를 클릭하세요.";
    private final String subject = "오늘의 모임, 회원 가입 인증";
    private final String type = "email-verify";
    private String token = "";

    @Override
    public void setToken(Accounts accounts) {
        this.token = accounts.getEmailCheckToken();
    }

    @Override
    public EmailsType getEmailType() {
        return EmailsType.VERIFY;
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
