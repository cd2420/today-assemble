package com.lim.assemble.todayassemble.validation;

import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.validation.factory.Validation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ValidationFactory {

    private final Map<ValidateType, Validation> validationMap = new HashMap<>();

    private ValidationFactory(List<Validation> validationList) {
        for(Validation validation: validationList) {
            validationMap.put(validation.getValidateType(), validation);
        }
    }

    public Validation createValidation(ValidateType validateType) {

        return validationMap.get(validateType);

    }

}
