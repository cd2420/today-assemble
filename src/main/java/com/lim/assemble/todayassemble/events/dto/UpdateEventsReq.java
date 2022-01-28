package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateEventsReq extends CreateEventsReq {

    @NotNull
    private Long id;

    @NotNull
    private Long accountsId;

}
