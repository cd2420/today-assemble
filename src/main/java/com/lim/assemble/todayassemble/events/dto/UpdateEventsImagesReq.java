package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UpdateEventsImagesReq extends UpdateEventsReqBase {

    @NotBlank
    private Set<EventsImagesDto> images;
}
