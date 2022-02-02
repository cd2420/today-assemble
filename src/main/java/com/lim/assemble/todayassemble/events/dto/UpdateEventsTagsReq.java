package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UpdateEventsTagsReq extends UpdateEventsReqBase {

    @NotBlank
    private Set<String> tags;

}
