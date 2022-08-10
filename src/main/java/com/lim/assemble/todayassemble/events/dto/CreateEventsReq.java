package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateEventsReq {

    @NotBlank
    @Size(min = 1, max = 50, message = ValidationMessage.WRONG_EVENTS_NAME_FORM)
    private String name;

    private String description;

    @NotNull
    @Min(value = 1, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    @Max(value = 50, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    private Integer maxMembers;

    @NotNull
    private EventsType eventsType;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Min(value = 1, message = ValidationMessage.CHECK_TAKE_TIME)
    @Max(value = 24, message = ValidationMessage.CHECK_TAKE_TIME)
    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Set<EventsImagesDto> eventsImagesSet;

    private Set<TagsDto> tagsSet;


}
