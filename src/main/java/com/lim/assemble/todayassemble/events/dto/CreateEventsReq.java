package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CreateEventsReq {

    @NotBlank
    private String name;

    @Lob
    private String description;

    @NotNull
    @Min(value = 1, message = "최소 인원은 1명입니다.")
    @Max(value = 30, message = "최대 인원은 30명입니다.")
    private Integer maxMembers;

    @NotNull
    private EventsType eventsType;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Min(value = 1 , message = "다시 시간을 체크하세요")
    @Max(value = 24 , message = "다시 시간을 체크하세요")
    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Set<EventsImagesDto> eventsImagesSet;

    private Set<TagsDto> tagsSet;

    private Set<ZoomsDto> zoomsSet;

}
