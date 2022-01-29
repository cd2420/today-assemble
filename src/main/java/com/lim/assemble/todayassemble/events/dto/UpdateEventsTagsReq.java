package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateEventsTagsReq extends UpdateEventsReqBase {

    private Set<String> tags;

}
