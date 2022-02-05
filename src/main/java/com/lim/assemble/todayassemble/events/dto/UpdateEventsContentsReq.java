package com.lim.assemble.todayassemble.events.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateEventsContentsReq extends UpdateEventsReqBase {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Integer maxMembers;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Min(value = 1 , message = "다시 시간을 체크하세요")
    @Max(value = 24 , message = "다시 시간을 체크하세요")
    private Long takeTime;

}
