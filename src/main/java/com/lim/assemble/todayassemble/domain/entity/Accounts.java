package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.BaseEntity;
import com.lim.assemble.todayassemble.type.AccountsType;
import com.lim.assemble.todayassemble.type.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Accounts extends BaseEntity {

    private String name;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private AccountsType accountType;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime birth;

    private Integer age;

    private Boolean emailVerified;

    private String emailCheckToken;
}
