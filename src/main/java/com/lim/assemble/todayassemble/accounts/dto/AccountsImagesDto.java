package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.AccountsImages;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsImagesDto {

    private ImagesType imagesType;

    private String image;

    public static AccountsImagesDto returnDto(AccountsImages accountsImages) {

        if (accountsImages == null) {
            return new AccountsImagesDto();
        }

        return new AccountsImagesDto(accountsImages.getImagesType(), accountsImages.getImage());
    }
}
