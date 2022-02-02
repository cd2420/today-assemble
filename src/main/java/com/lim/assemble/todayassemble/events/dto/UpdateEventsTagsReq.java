package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UpdateEventsTagsReq extends UpdateEventsReqBase {

    @NotNull
    private Set<String> tags;

}
