package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class CreateEventsReq {

    @NotNull
    private String name;

    @Lob
    private String description;

    @NotNull
    private Integer maxMembers;

    @NotNull
    private EventsType eventsType;

    @NotNull
    private LocalDateTime eventsTime;

    @NotNull
    @Min(value = 1 , message = "다시 시간을 체크하세요")
    @Max(value = 24 , message = "다시 시간을 체크하세요")
    private Long takeTime;

    @NotNull
    private String address;

    @NotNull
    private String longitude;

    @NotNull
    private String latitude;

    private Set<EventsImagesDto> eventsImagesSet = new HashSet<>();

    private Set<TagsDto> tagsSet = new HashSet<>();

    private Set<ZoomsDto> zoomsSet = new HashSet<>();

    // TODO 장소 데이터

}
