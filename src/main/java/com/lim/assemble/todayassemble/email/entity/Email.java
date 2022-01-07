package com.lim.assemble.todayassemble.email.entity;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.type.EmailsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "email")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    private Accounts accounts;

    @Enumerated(EnumType.STRING)
    private EmailsType emailType;

}
