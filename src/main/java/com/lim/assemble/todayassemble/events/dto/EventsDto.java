package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.EventsImages;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class EventsDto {

    private Long id;

    private String name;

    private String hostName;

    private String description;

    private Integer maxMembers;

    private Integer likes;

    private EventsType eventsType;

    private LocalDateTime eventsTime;

    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Set<EventsImages> eventsImagesSet;

    private Set<Tags> tagsSet;

    private Set<Zooms> zoomsSet;


    public static EventsDto from(Events events) {
        return EventsDto.builder()
                .id(events.getId())
                .name(events.getName())
                .hostName(events.getHostName())
                .description(events.getDescription())
                .maxMembers(events.getMaxMembers())
                .likes(events.getLikes())
                .eventsType(events.getEventsType())
                .eventsTime(events.getEventsTime())
                .takeTime(events.getTakeTime())
                .address(events.getAddress())
                .longitude(events.getLongitude())
                .latitude(events.getLatitude())
                .eventsImagesSet(events.getEventsImagesSet() == null ? new HashSet<>() : events.getEventsImagesSet())
                .tagsSet(events.getTagsSet() == null ? new HashSet<>() : events.getTagsSet())
                .zoomsSet(events.getZoomsSet() == null ? new HashSet<>() : events.getZoomsSet())
                .build();
    }
}
