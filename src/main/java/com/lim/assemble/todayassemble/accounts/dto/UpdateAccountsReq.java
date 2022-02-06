package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class UpdateAccountsReq {

    @Size(min = 3, max = 10, message = ValidationMessage.WRONG_ACCOUNTS_NAME_FORM)
    private String name;

    @Size(min = 6, max = 20, message = ValidationMessage.WRONG_PASSWORD_FORM)
    private String password;

}
