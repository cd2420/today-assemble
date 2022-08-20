package com.lim.assemble.todayassemble.accounts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.common.type.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class AccountsDto {

    private Long id;

    private String name;

    private String email;

    private Gender gender;

    private Integer age;

    private AccountsImagesDto accountsImagesDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime birth;

    private Boolean emailVerified;

    public static AccountsDto from(Accounts accounts) {
        return AccountsDto.builder()
                .id(accounts.getId())
                .name(accounts.getName())
                .email(accounts.getEmail())
                .gender(accounts.getGender())
                .age(accounts.getAge())
                .accountsImagesDto(new AccountsImagesDto())
                .birth(accounts.getBirth())
                .emailVerified(accounts.getEmailVerified())
                .build();
    }

    public static AccountsDto from(AccountsMapperEvents accountsMapperEvents) {
        Accounts accounts = accountsMapperEvents.getAccounts();

        return AccountsDto.builder()
                .id(accounts.getId())
                .name(accounts.getName())
                .email(accounts.getEmail())
                .build();
    }

    public static AccountsDto from(Accounts accounts, Boolean returnDataWithImages) {
        AccountsDto accountsDto = from(accounts);
        if (returnDataWithImages) {
            accountsDto.setAccountsImagesDto(AccountsImagesDto.returnDto(accounts.getAccountsImages()));
        }
        return accountsDto;
    }

    public static Set<AccountsDto> returnAccountsDtoSet(Set<AccountsMapperEvents> accountsEventsSet) {
        if (accountsEventsSet == null) {
            return new HashSet<>();
        }
        return accountsEventsSet.stream()
                .map(AccountsDto::from)
                .collect(Collectors.toSet());
    }


}
