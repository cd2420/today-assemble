package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.common.type.ValidateType;

public interface Validation {

    <T> T validate(T target);
    ValidateType getValidateType();
}
