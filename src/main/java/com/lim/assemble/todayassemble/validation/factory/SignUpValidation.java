package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignUpValidation implements Validation {

    private ValidateType validateType = ValidateType.SIGNUP;

    @Autowired
    private AccountsRepository accountsRepository;


    @Override
    public void validate(Object target) {
        CreateAccountReq createAccountReq = (CreateAccountReq) target;
        accountsRepository.findByEmail(createAccountReq.getEmail())
                .ifPresent(check -> {
                    throw new TodayAssembleException(ErrorCode.ALREADY_EXISTS_USER);
                });
    }

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }
}
