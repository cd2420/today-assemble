package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.Images;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accounts_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsImages extends Images {

    @OneToOne
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;
}
