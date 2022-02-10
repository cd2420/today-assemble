package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountsEventsDto {

    private AccountsDto accountsDto;

    public static AccountsEventsDto from(Accounts accounts) {
        return AccountsEventsDto.builder()
                .accountsDto(AccountsDto.from(accounts))
                .build();

    }
}
