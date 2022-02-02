package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class CreateEventsReq {

    @NotBlank
    private String name;

    @Lob
    private String description;

    @NotBlank
    private Integer maxMembers;

    @NotBlank
    private EventsType eventsType;

    @NotBlank
    private LocalDateTime eventsTime;

    @NotBlank
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
