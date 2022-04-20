package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.common.service.CommonService;
import com.lim.assemble.todayassemble.common.type.EmailsType;
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
    private final CommonService commonService;

    @Override
    public Object sendEmail(Accounts accounts, EmailsType emailsType) {
        switch (emailsType) {
            case VERIFY:
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
                .emailType(emailsType)
                .build();

    }

    private EmailMessage getEmailMessage(Accounts accounts, EmailsType emailsType) {

        String url = "";
        String linkName = "";
        String payLoad = "";
        String subject = "";
        String token = "";
        String type = "";

        if (emailsType == EmailsType.VERIFY) {
            url = "home";
            linkName = "이메일 인증하기";
            payLoad = "오늘의 모임 서비스를 사용하려면 링크를 클릭하세요.";
            subject = "오늘의 모임, 회원 가입 인증";
            token = accounts.getEmailCheckToken();
            type = "email-verify";
        } else {
            url = "home";
            linkName = "이메일 로그인하기";
            payLoad = "오늘의 모임 로그인 하시려면 링크를 클릭하세요.";
            subject = "오늘의 모임, 이메일로 로그인";
            token = accounts.getEmailLoginToken();
            type = "email-login";
        }

        Context context = new Context();
        context.setVariable(
                "link"
                , "/" + url
                        + "?token="  + token
                        + "&email=" + accounts.getEmail()
                        + "&type="  + type
        );
        context.setVariable("nickname", accounts.getName());
        context.setVariable("linkName", linkName);
        context.setVariable("message", payLoad);
        context.setVariable("host", appProperties.getFront_host());
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
    public AccountsDto emailToken(
            String email
            , String token
            , EmailsType emailsType
            , HttpServletResponse response) {
        Accounts accounts = accountsRepository.findByEmail(email)
                .orElseThrow( () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));

        boolean is_error = false;

        if (EmailsType.VERIFY == emailsType) {
            if (accounts.getEmailCheckToken().equals(token)) {
                accounts.setEmailVerified(true);
                accounts.setEmailCheckToken(null);
            } else {
                is_error = true;
            }

        } else {
            if (accounts.getEmailLoginToken().equals(token)) {
                accounts.setEmailLoginVerified(true);
                accounts.setEmailLoginToken(null);
            } else {
                is_error = true;
            }
        }

        if (is_error) {
            throw new TodayAssembleException(ErrorCode.WRONG_EMAIL_TOKEN);
        }

        commonService.loginWithToken(response, accounts);
        return AccountsDto.from(accounts);
    }

}
