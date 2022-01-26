package com.lim.assemble.todayassemble.validation;

import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.validation.factory.LoginValidation;
import com.lim.assemble.todayassemble.validation.factory.SignUpValidation;
import com.lim.assemble.todayassemble.validation.factory.Validation;
import lombok.RequiredArgsConstructor;
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
        if (validateType == ValidateType.SIGNUP) {
            return validationMap.get(ValidateType.SIGNUP);
        } else if (validateType == ValidateType.LOGIN){
            return validationMap.get(ValidateType.LOGIN);
        } else {
            return validationMap.get(ValidateType.EVENT);
        }

    }

}
