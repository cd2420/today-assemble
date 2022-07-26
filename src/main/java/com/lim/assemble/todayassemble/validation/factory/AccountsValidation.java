package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.UpdateAccountsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccountsValidation implements Validation {

    private final AccountsRepository accountsRepository;

    private ValidateType validateType = ValidateType.ACCOUNT;

    @Override
    @Transactional(readOnly = true)
    public void validate(ValidateSituationType validateSituationType, Object... target) {
        if (ValidateSituationType.CREATE.equals(validateSituationType)) {
            checkOverlappingAccountsEmail((CreateAccountReq) target[0]);
        } else if (ValidateSituationType.UPDATE.equals(validateSituationType)) {
            checkAccountsIdEquals((UpdateAccountsDto) target[0]);
        } else {
            checkAccountsIdEquals((UpdateAccountsDto) target[0]);
        }

    }

    private void checkAccountsIdEquals(UpdateAccountsDto target) {

        if (!target.getTargetId().equals(target.getCheckId())) {
            throw new TodayAssembleException(ErrorCode.NOT_EQUAL_ACCOUNT);
        }

    }

    private void checkOverlappingAccountsEmail(CreateAccountReq createAccountReq) {
        accountsRepository.findByEmail(createAccountReq.getEmail(), Accounts.class)
                .ifPresent(check -> {
                    throw new TodayAssembleException(ErrorCode.ALREADY_EXISTS_USER);
                });
    }

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }
}
