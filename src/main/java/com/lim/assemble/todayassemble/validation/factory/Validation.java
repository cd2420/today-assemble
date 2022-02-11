package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;

public interface Validation {

    void validate(ValidateSituationType validateSituationType, Object... target);
    ValidateType getValidateType();
}
