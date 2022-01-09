package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.common.type.ValidateType;
import org.springframework.stereotype.Component;

@Component
public class LoginValidation implements Validation {

    private ValidateType validateType = ValidateType.LOGIN;

    @Override
    public <T> T validate(T target) {
        return null;
    }

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }
}
