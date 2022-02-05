package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.UpdateAccountsDto;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountsValidation implements Validation {

    private final AccountsRepository accountsRepository;

    private ValidateType validateType = ValidateType.ACCOUNT;

    @Override
    public void validate(Object target) {
        if (CreateAccountReq.class.equals(target.getClass())) {
            checkOverlappingAccountsEmail((CreateAccountReq) target);
        } else {
            checkAccountsIdEquals((UpdateAccountsDto) target);
        }

    }

    private void checkAccountsIdEquals(UpdateAccountsDto target) {

        if (!target.getTargetId().equals(target.getCheckId())) {
            throw new TodayAssembleException(ErrorCode.NOT_EQUAL_ACCOUNT);
        }

    }

    private void checkOverlappingAccountsEmail(CreateAccountReq createAccountReq) {
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
