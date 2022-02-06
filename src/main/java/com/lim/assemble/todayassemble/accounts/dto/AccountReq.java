package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AccountReq {

    @NotBlank
    @Email(message = ValidationMessage.WRONG_EMAIL_FORM)
    private String email;

    @NotBlank
    @Size(min = 6, max = 20, message = ValidationMessage.WRONG_PASSWORD_FORM)
    private String password;
}
