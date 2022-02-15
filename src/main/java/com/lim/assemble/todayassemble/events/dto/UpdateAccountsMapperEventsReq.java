package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAccountsMapperEventsReq {

    @NotNull
    private Boolean response;
}
