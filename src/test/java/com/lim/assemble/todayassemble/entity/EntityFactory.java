package com.lim.assemble.todayassemble.entity;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsImages;
import com.lim.assemble.todayassemble.common.type.*;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.EventsImages;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.places.entity.Places;
import com.lim.assemble.todayassemble.places.entity.PlacesImages;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;

import java.time.LocalDateTime;

public class EntityFactory {

    public static Object createEntity(Entity_Type entity_type) {

        if (entity_type == Entity_Type.ACCOUNTS) {
            return createAccounts();
        } else if(entity_type == Entity_Type.ACCOUNTS_IMAGES) {
            return createAccountsImages(createAccounts());
        } else if(entity_type == Entity_Type.EVENTS) {
            return createEvents(createAccounts(), createPlaces());
        } else if(entity_type == Entity_Type.PLACES) {
            return createPlaces();
        } else if(entity_type == Entity_Type.EVENTS_IMAGES) {
            return createEventsImages(
                    createEvents(
                            createAccounts()
                            , createPlaces()
                    )
            );
        } else if(entity_type == Entity_Type.PLACES_IMAGES) {
            return createPlacesImages(createPlaces());
        } else if(entity_type == Entity_Type.EMAIL) {
            return createEmail(createAccounts());
        } else if(entity_type == Entity_Type.LIKES) {
            return createLikes(
                    createEvents(createAccounts(), createPlaces())
            );
        } else if(entity_type == Entity_Type.TAGS) {
            return createTags(
                    createEvents(createAccounts(), createPlaces())
            );
        } else {
            return createZooms(
                    createEvents(createAccounts(), null)
            );
        }
    }

    private static Zooms createZooms(Events events) {
        return Zooms.builder()
                .events(events)
                .url("~~~~/~~~/~~~/~~~.com")
                .status(true)
                .build();
    }

    private static Tags createTags(Events events) {
        return Tags.builder()
                .events(events)
                .name("영화")
                .build();
    }

    private static Likes createLikes(Events events) {
        return Likes.builder()
                .accounts(events.getAccounts())
                .events(events)
                .build();
    }

    private static Email createEmail(Accounts accounts) {
        return Email.builder()
                .accounts(accounts)
                .emailType(EmailsType.SIGNUP)
                .build();
    }

    private static PlacesImages createPlacesImages(Places places) {
        return PlacesImages.builder()
                .imagesType(ImagesType.MAIN)
                .image("places image path ~~~~~~")
                .places(places)
                .build();
    }

    private static EventsImages createEventsImages(Events events) {
        return EventsImages.builder()
                .imagesType(ImagesType.MAIN)
                .image("events image path ~~~~~~")
                .events(events)
                .build();
    }

    private static Places createPlaces() {
        return Places.builder()
                .name("홍대CGV")
                .address("서울 ~~ ~~ ~~")
                .latitude("12312932")
                .longtitude("112031123")
                .build();
    }

    private static Events createEvents(Accounts accounts, Places places) {
        return Events.builder()
                .name("스파이더맨 보러갈 모임")
                .host_email(accounts.getEmail())
                .description("영화 모임~~~~~~~~~~")
                .accounts(accounts)
                .places(places)
                .maxMembers(4)
                .likes(0)
                .eventsType(places != null ? EventsType.OFFLINE : EventsType.ONLINE)
                .build();
    }

    private static AccountsImages createAccountsImages(Accounts accounts) {
        return AccountsImages.builder()
                .accounts(accounts)
                .image("path~~~~~~~~")
                .imagesType(ImagesType.MAIN)
                .build();
    }

    private static Accounts createAccounts() {
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
    }
}
