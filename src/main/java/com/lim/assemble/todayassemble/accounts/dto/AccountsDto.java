package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsImages;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class AccountsDto {

    private String name;

    private String email;

    private AccountsType accountType;

    private Gender gender;

    private Set<Events> eventsSet;

    private Set<Email> emailSet;

    private Set<Likes> likesSet;

    private AccountsImages accountsImages;

    private Boolean emailVerified;

    private String emailCheckToken;

    public static AccountsDto from(Accounts accounts) {
        return AccountsDto.builder()
                .name(accounts.getName())
                .email(accounts.getEmail())
                .accountType(accounts.getAccountType())
                .gender(accounts.getGender())
                .eventsSet(accounts.getEventsSet() != null ? accounts.getEventsSet() : new HashSet<Events>())
                .emailSet(accounts.getEmailSet() != null ? accounts.getEmailSet() : new HashSet<Email>())
                .likesSet(accounts.getLikesSet() != null ? accounts.getLikesSet() : new HashSet<Likes>())
                .accountsImages(accounts.getAccountsImages())
                .emailVerified(accounts.getEmailVerified())
                .emailCheckToken(accounts.getEmailCheckToken())
                .build();
    }
}
