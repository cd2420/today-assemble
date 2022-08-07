package com.lim.assemble.todayassemble.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lim.assemble.todayassemble.common.entity.Images;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts_images")
@Getter
@NoArgsConstructor
public class AccountsImages extends Images {

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "accounts_id")
//    @JsonIgnore
//    private Accounts accounts;

    @Builder
    public AccountsImages(
            ImagesType imagesType
            , String image
            , Accounts accounts) {

        super(imagesType, image);
//        this.accounts = accounts;
    }

}
