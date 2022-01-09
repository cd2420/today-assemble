package com.lim.assemble.todayassemble.email.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.common.property.AppProperties;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import com.lim.assemble.todayassemble.email.dto.EmailMessage;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class RealEmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final AppProperties appProperties;
    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(AccountsDto accountsDto, EmailsType emailsType) {
        switch (emailsType) {
            case SIGNUP:
                sendSignUpEmail(accountsDto);
                break;
            default:
                break;
        }
    }

    private void sendSignUpEmail(AccountsDto accountsDto) {
        EmailMessage emailMessage = getEmailMessage(accountsDto);

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

    }

    private EmailMessage getEmailMessage(AccountsDto accountsDto) {
        Context context = new Context();
        context.setVariable(
                "link"
                , "/email/check-email-token?token=" + accountsDto.getEmailCheckToken()
                        + "&email=" + accountsDto.getEmail()
        );
        context.setVariable("nickname", accountsDto.getName());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "오늘의 모임 서비스를 사용하려면 링크를 클릭하세요.");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(accountsDto.getEmail())
                .subject("오늘의 모임, 회원 가입 인증")
                .message(message)
                .build();
        return emailMessage;
    }
}
