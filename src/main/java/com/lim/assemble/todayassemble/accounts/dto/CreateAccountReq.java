package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
public class CreateAccountReq extends AccountReq {

    @NotBlank
    @Size(min = 3, max = 10, message = "이름길이 초과")
    private String name;

    @NotBlank
    private Gender gender;

    @NotBlank
    private LocalDateTime birth;

    @NotBlank
    private Integer age;

}
