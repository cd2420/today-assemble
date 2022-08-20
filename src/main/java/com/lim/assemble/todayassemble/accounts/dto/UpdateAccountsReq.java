package com.lim.assemble.todayassemble.accounts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.Data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateAccountsReq {

    @Size(min = 3, max = 10, message = ValidationMessage.WRONG_ACCOUNTS_NAME_FORM)
    private String name;

    @Nullable
    @Size(min = 6, max = 20, message = ValidationMessage.WRONG_PASSWORD_FORM)
    private String password;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDateTime birth;

    @NotNull
    private Integer age;

    private AccountsImagesDto accountsImagesDto;

}
