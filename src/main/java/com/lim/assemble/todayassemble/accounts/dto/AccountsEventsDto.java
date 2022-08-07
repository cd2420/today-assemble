package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountsEventsDto<T> {

    private T returnDto;

    public static <T extends Accounts> AccountsEventsDto from(T t) {

        return AccountsEventsDto.builder()
                .returnDto(AccountsDto.from(t, false))
                .build();

    }

    public static <T extends Events> AccountsEventsDto from(T t) {

        return AccountsEventsDto.builder()
                .returnDto(EventsDto.from(t))
                .build();

    }

}
