package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.AccountsImages;
import com.lim.assemble.todayassemble.domain.entity.Events;
import com.lim.assemble.todayassemble.domain.entity.Places;
import com.lim.assemble.todayassemble.type.AccountsType;
import com.lim.assemble.todayassemble.type.EventsType;
import com.lim.assemble.todayassemble.type.Gender;
import com.lim.assemble.todayassemble.type.ImagesType;

import java.time.LocalDateTime;

public class EntityFactory {

    public static Object createEntity(Entity_Type entity_type) {

        if (entity_type == Entity_Type.ACCOUNTS) {
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

        } else if(entity_type == Entity_Type.ACCOUNTS_IMAGES) {
            return AccountsImages.builder()
                    .accounts((Accounts) createEntity(Entity_Type.ACCOUNTS))
                    .image("path~~~~~~~~")
                    .imagesType(ImagesType.MAIN)
                    .build();
        } else if(entity_type == Entity_Type.EVENTS) {
            Accounts accounts = (Accounts) createEntity(Entity_Type.ACCOUNTS);
            Places places = (Places) createEntity(Entity_Type.PLACES);
            return Events.builder()
                    .name("스파이더맨 보러갈 모임")
                    .host_email(accounts.getEmail())
                    .description("영화 모임~~~~~~~~~~")
                    .accounts(accounts)
                    .places(places)
                    .maxMembers(4)
                    .likes(0)
                    .eventsType(EventsType.OFFLINE)
                    .build();
        } else {
            return Places.builder()
                    .name("홍대CGV")
                    .address("서울 ~~ ~~ ~~")
                    .latitude("12312932")
                    .longtitude("112031123")
                    .build();
        }
    }
}
