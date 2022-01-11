package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
public class CreateAccountReq extends AccountReq {

    @NotNull
    @Size(min = 3, max = 10, message = "이름길이 초과")
    private String name;

    @NotNull
    private Gender gender;

//    @NotNull test때문에 잠시 주석처리
    private LocalDateTime birth;

    @NotNull
    private Integer age;

}
