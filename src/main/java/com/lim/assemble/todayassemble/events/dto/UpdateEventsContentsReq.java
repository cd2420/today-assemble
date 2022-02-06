package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.message.ValidationMessage;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class UpdateEventsContentsReq extends UpdateEventsReqBase {

    @NotBlank
    @Size(min = 1, max = 50, message = ValidationMessage.WRONG_EVENTS_NAME_FORM)
    private String name;

    private String description;

    @NotNull
    @Size(min = 1, max = 30, message = ValidationMessage.WRONG_MAXNUMBERS_SIZE)
    private Integer maxMembers;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Size(min = 1, max = 24, message = ValidationMessage.CHECK_TAKE_TIME)
    private Long takeTime;

}
