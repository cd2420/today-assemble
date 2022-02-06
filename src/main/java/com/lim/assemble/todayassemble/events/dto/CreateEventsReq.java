package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CreateEventsReq {

    @NotBlank
    @Size(min = 1, max = 50, message = ValidationMessage.WRONG_EVENTS_NAME_FORM)
    private String name;

    private String description;

    @NotNull
    @Size(min = 1, max = 30, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    private Integer maxMembers;

    @NotNull
    private EventsType eventsType;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Size(min = 1, max = 24, message = ValidationMessage.CHECK_TAKE_TIME)
    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Set<EventsImagesDto> eventsImagesSet;

    private Set<TagsDto> tagsSet;

    private Set<ZoomsDto> zoomsSet;

}
