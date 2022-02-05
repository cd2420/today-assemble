package com.lim.assemble.todayassemble.accounts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.UpdateAccountsReq;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.events.entity.Events;
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

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Events> eventsSet = new HashSet<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    @Setter
    private Set<Email> emailSet = new HashSet<>();

    @OneToMany(mappedBy = "accounts", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Likes> likesSet = new HashSet<>();

    @OneToOne(mappedBy = "accounts", fetch = FetchType.LAZY)
    @JsonBackReference
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

    public void update(UpdateAccountsReq updateAccountsReq) {
        this.name = updateAccountsReq.getName();
        this.password = updateAccountsReq.getPassword();

    }
}
