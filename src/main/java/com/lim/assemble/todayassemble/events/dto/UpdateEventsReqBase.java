package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateEventsReqBase {

    @NotBlank
    private Long id;

    private Long accountsId;

}
