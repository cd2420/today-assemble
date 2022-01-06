package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.Images;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accounts_images")
@Getter
public class AccountsImages extends Images {

    @OneToOne
    @JoinColumn(name = "accounts_id")
    private Accounts accounts;

    @Builder
    public AccountsImages(
            ImagesType imagesType
            , String image
            , Accounts accounts) {

        super(imagesType, image);
        this.accounts = accounts;
    }

    public AccountsImages() {
        super();
    }

}
