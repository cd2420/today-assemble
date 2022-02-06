package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
public class CreateAccountReq extends AccountReq {

    @NotBlank
    @Size(min = 3, max = 10, message = ValidationMessage.WRONG_ACCOUNTS_NAME_FORM)
    private String name;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDateTime birth;

    @NotNull
    private Integer age;

}
