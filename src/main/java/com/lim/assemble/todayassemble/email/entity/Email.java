package com.lim.assemble.todayassemble.email.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.EmailsType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "email")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email extends BaseEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    @JsonIgnore
    private Accounts accounts;

    @Enumerated(EnumType.STRING)
    private EmailsType emailType;

}
