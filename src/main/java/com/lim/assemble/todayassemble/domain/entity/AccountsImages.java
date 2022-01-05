package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.Images;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "accounts_images")
@Getter
@SuperBuilder
public class AccountsImages extends Images {

    @OneToOne
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;


}
