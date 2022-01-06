package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.AccountsImages;
import com.lim.assemble.todayassemble.type.AccountsType;
import com.lim.assemble.todayassemble.type.Gender;
import com.lim.assemble.todayassemble.type.ImagesType;

import java.time.LocalDateTime;

public class EntityFactory {

    public static Object createEntity(Entity_Type accounts) {

        if (accounts == Entity_Type.ACCOUNTS) {
            return Accounts.builder()
                    .name("limdaegeun")
                    .email("asdw@asdw.com")
                    .password("adwdzcxasd")
                    .accountType(AccountsType.CLIENT)
                    .gender(Gender.MALE)
                    .birth(LocalDateTime.now())
                    .age(30)
                    .emailVerified(true)
                    .emailCheckToken("asdwdadwasdw")
                    .build();

        } else {
            return AccountsImages.builder()
                    .accounts((Accounts) createEntity(Entity_Type.ACCOUNTS))
                    .image("path~~~~~~~~")
                    .imagesType(ImagesType.MAIN)
                    .build();
        }




    }
}
