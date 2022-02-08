package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class EventsDto {

    private Long id;

    private String name;

    private Long hostAccountsId;

    private String description;

    private Integer maxMembers;

    private Integer likes;

    private EventsType eventsType;

    private LocalDateTime eventsTime;

    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Set<EventsImagesDto> eventsImagesDtos;

    private Set<TagsDto> tagsDtos;

    private Set<ZoomsDto> zoomsDtos;


    public static EventsDto from(Events events) {
        return EventsDto.builder()
                .id(events.getId())
                .name(events.getName())
                .hostAccountsId(events.getHostAccountsId())
                .description(events.getDescription())
                .maxMembers(events.getMaxMembers())
                .likes(events.getLikesSet() == null ? 0 : events.getLikesSet().size())
                .eventsType(events.getEventsType())
                .eventsTime(events.getEventsTime())
                .takeTime(events.getTakeTime())
                .address(events.getAddress())
                .longitude(events.getLongitude())
                .latitude(events.getLatitude())
                .eventsImagesDtos(EventsImagesDto.returnEventsImagesDtoSet(events.getEventsImagesSet()))
                .tagsDtos(TagsDto.returnTagsDtoSet(events.getTagsSet()))
                .zoomsDtos(ZoomsDto.returnZoomsDtoSet(events.getZoomsSet()))
                .build();
    }

    public static Set<EventsDto> returnEventsDtoSet(Set<Events> eventsSet) {
        if (eventsSet == null) {
            return new HashSet<>();
        }
        return eventsSet.stream()
                        .map(EventsDto::from)
                        .collect(Collectors.toSet());

    }
}
