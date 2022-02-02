package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
public class UpdateEventsTypeReq extends UpdateEventsReqBase {

    @NotBlank
    private EventsType eventsType;

    private String address;

    private String longitude;

    private String latitude;

    private Set<ZoomsDto> zooms;

}
