package com.lim.assemble.todayassemble.accounts.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter @Setter
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

    private LocalDateTime emailCheckTokenGeneratedAt;

    private Boolean emailLoginVerified;

    private String emailLoginToken;

    private LocalDateTime emailLoginTokenGeneratedAt;

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<AccountsMapperEvents> accountsEventsSet = new HashSet<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Setter
    private Set<Email> emailSet = new HashSet<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Likes> likesSet = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AccountsImages accountsImages;

    public static Accounts from(CreateAccountReq createAccountReq) {
        return Accounts.builder()
                .name(createAccountReq.getName())
                .email(createAccountReq.getEmail())
                .password(createAccountReq.getPassword())
                .gender(createAccountReq.getGender())
                .birth(createAccountReq.getBirth())
                .age(createAccountReq.getAge())
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.accountType = this.accountType == null ? AccountsType.CLIENT : this.accountType;
    }

    public Accounts generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
        this.emailVerified = false;
        return this;
    }

    public Accounts generateLoginEmailToken() {
        this.emailLoginToken = UUID.randomUUID().toString();
        this.emailLoginTokenGeneratedAt = LocalDateTime.now();
        this.emailLoginVerified = false;
        return this;
    }

}
