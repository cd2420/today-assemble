package com.lim.assemble.todayassemble.accounts.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AccountReq {

    @NotNull
    @Email(message = "이메일 형식 불일치")
    private String email;

    @NotNull
    @Size(min = 6, message = "6자리 이상 입력해 주세요.")
    private String password;
}
