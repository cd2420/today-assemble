package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.likes.dto.LikesDto;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AccountsDto {

    private String name;

    private String email;

    private AccountsType accountType;

    private Gender gender;

    private Set<EventsDto> eventsDtos;

    private Set<LikesDto> likesDtos;

    private AccountsImagesDto accountsImagesDto;

    private Boolean emailVerified;

    private String emailCheckToken;

    public static AccountsDto from(Accounts accounts) {
        return AccountsDto.builder()
                .name(accounts.getName())
                .email(accounts.getEmail())
                .accountType(accounts.getAccountType())
                .gender(accounts.getGender())
                .eventsDtos(EventsDto.returnEventsDtoSet(accounts.getEventsSet()))
                .likesDtos(LikesDto.returnLikesDtoSet(accounts.getLikesSet()))
                .accountsImagesDto(AccountsImagesDto.returnDto(accounts.getAccountsImages()))
                .emailVerified(accounts.getEmailVerified())
                .emailCheckToken(accounts.getEmailCheckToken())
                .build();
    }
}
