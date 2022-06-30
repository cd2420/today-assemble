package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import com.lim.assemble.todayassemble.common.type.EventsType;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UpdateEventsContentsReq extends UpdateEventsImagesReq {

    @NotBlank
    @Size(min = 1, max = 50, message = ValidationMessage.WRONG_EVENTS_NAME_FORM)
    private String name;

    private String description;

    @NotNull
    @Min(value = 1, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    @Max(value = 30, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    private Integer maxMembers;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Min(value = 1, message = ValidationMessage.CHECK_TAKE_TIME)
    @Max(value = 24, message = ValidationMessage.CHECK_TAKE_TIME)
    private Long takeTime;

    private Set<String> tags;

    private Set<EventsImagesDto> images;

//    @NotNull
    private EventsType eventsType;

    private String address;

    private String longitude;

    private String latitude;

}
