package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UpdateEventsTagsReq {

    @NotNull
    private Long id;

    @NotNull
    private Long accountsId;

    private Set<String> tags;

}
