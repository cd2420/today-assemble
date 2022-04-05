package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateEventsImagesReq extends UpdateEventsReqBase {

    private Set<EventsImagesDto> images;
}
