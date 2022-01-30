package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateEventsReqBase {

    @NotNull
    private Long id;

    private Long accountsId;

}
