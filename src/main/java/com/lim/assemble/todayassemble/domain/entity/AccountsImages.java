package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.BaseEntity;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsImages extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ImagesType imagesType;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @OneToOne
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;


}
