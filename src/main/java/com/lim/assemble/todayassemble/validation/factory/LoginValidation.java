package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.dto.LoginAccountReq;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginValidation implements Validation {

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    private ValidateType validateType = ValidateType.LOGIN;

    @Override
    public void validate(Object target) {
        LoginAccountReq loginAccountReq = (LoginAccountReq) target;
        Accounts accounts = accountsRepository.findByEmail(loginAccountReq.getEmail())
                        .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));

        // 패스워드 체크
        if (!passwordEncoder.matches(loginAccountReq.getPassword(), accounts.getPassword())
        ) {
            throw new TodayAssembleException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        // 이메일 인증 체크
        if (!accounts.getEmailVerified()) {
            throw new TodayAssembleException(ErrorCode.NOT_GET_EMAIL_VERIFIED);
        }
    }

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }
}
