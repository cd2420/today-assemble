package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateAccountReq {

    @NotNull
    @Size(min = 3, max = 10, message = "이름길이 초과")
    private String name;

    @NotNull
    @Email(message = "이메일 형식 불일치")
    private String email;

    @NotNull
    @Size(min = 6, message = "6자리 이상 입력해 주세요.")
    private String password;

    @Builder.Default
    private AccountsType accountType = AccountsType.CLIENT;

    @NotNull
    private Gender gender;

    @NotNull
    private LocalDateTime birth;

    @NotNull
    private Integer age;

    @Builder.Default
    private Boolean emailVerified = false;

}
