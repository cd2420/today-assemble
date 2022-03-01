package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.config.AuthenticationService;
import com.lim.assemble.todayassemble.email.dto.EmailMessage;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.email.repository.EmailRepository;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class RealEmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    private final EmailRepository emailRepository;
    private final AccountsRepository accountsRepository;

    @Override
    public Object sendEmail(Accounts accounts, EmailsType emailsType) {
        switch (emailsType) {
            case SIGNUP:
            case LOGIN:
                return getEmail(accounts, emailsType);
            default:
                return null;
        }
    }

    private Email getEmail(Accounts accounts, EmailsType emailsType) {
        EmailMessage emailMessage = getEmailMessage(accounts, emailsType);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mimeMessage, false, "UTF-8"
            );
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(emailMessage.getMessage(), true);
            javaMailSender.send(mimeMessage);
            log.info("sent email: {}", emailMessage.getMessage());
        } catch (Exception e) {
            log.error("failed to send email", e);
            throw new TodayAssembleException(ErrorCode.FAIL_TO_SEND_EMAIL);
        }
        return Email.builder()
                .accounts(accounts)
                .emailType(EmailsType.SIGNUP)
                .build();

    }

    private EmailMessage getEmailMessage(Accounts accounts, EmailsType emailsType) {

        String url = "";
        String linkName = "";
        String payLoad = "";
        String subject = "";
        String token = "";

        if (emailsType == EmailsType.SIGNUP) {
            url = "check-email-token";
            linkName = "이메일 인증하기";
            payLoad = "오늘의 모임 서비스를 사용하려면 링크를 클릭하세요.";
            subject = "오늘의 모임, 회원 가입 인증";
            token = accounts.getEmailCheckToken();
        } else {
            url = "login-email-token";
            linkName = "이메일 로그인하기";
            payLoad = "오늘의 모임 로그인 하시려면 링크를 클릭하세요.";
            subject = "오늘의 모임, 이메일로 로그인";
            token = accounts.getEmailLoginToken();
        }

        Context context = new Context();
        context.setVariable(
                "link"
                , "/api/v1/email/" + url +"?token=" + token
                        + "&email=" + accounts.getEmail()
        );
        context.setVariable("nickname", accounts.getName());
        context.setVariable("linkName", linkName);
        context.setVariable("message", payLoad);
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(accounts.getEmail())
                .subject(subject)
                .message(message)
                .build();
        return emailMessage;
    }

    @Override
    @Transactional
    public void verify(String email, String token) {

        Accounts accounts = accountsRepository.findByEmail(email)
                            .orElseThrow( () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));

        if (!accounts.getEmailCheckToken().equals(token)) {
            throw new TodayAssembleException(ErrorCode.WRONG_EMAIL_TOKEN);
        } else {
            accounts.setEmailVerified(true);
        }

    }

    @Override
    public boolean checkLoginToken(String email, String token, HttpServletResponse response) {
        Accounts accounts = accountsRepository.findByEmail(email)
                .orElseThrow( () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));

        if (!accounts.getEmailLoginToken().equals(token)) {
            return false;
        }
        AuthenticationService.addJWTToken(response, email);
        return true;
    }
}
