package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UpdateEventsImagesReq extends UpdateEventsReqBase {

    @NotNull
    private Set<EventsImagesDto> images;
}
