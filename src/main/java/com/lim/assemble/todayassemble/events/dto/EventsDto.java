package com.lim.assemble.todayassemble.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.likes.dto.LikesAccountsDto;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime eventsTime;

    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    private Integer nowMembers;

    private Set<LikesAccountsDto> likesAccountsDtos;

    private Set<EventsImagesDto> eventsImagesDtos;

    private Set<TagsDto> tagsDtos;

    private Set<AccountsDto> accountsDtos;

    public static EventsDto from(Events events) {
        return getEventsDto(events);
    }

    public static EventsDto from(AccountsMapperEvents accountsMapperEvents) {
        Events events = accountsMapperEvents.getEvents();
        return getEventsDto(events);
    }

    private static EventsDto getEventsDto(Events events) {
        return EventsDto.builder()
                .id(events.getId())
                .name(events.getName())
                .hostAccountsId(events.getAccounts().getId())
                .description(events.getDescription())
                .maxMembers(events.getMaxMembers())
                .likes(events.getLikesSet() == null ? 0 : events.getLikesSet().size())
                .eventsType(events.getEventsType())
                .eventsTime(events.getEventsTime())
                .takeTime(events.getTakeTime())
                .address(events.getAddress())
                .longitude(events.getLongitude())
                .latitude(events.getLatitude())
                .nowMembers(events.getAccountsEventsSet().size())
                .likesAccountsDtos(LikesAccountsDto.returnLikesDtoSet(events.getLikesSet()))
                .eventsImagesDtos(EventsImagesDto.returnEventsImagesDtoSet(events.getEventsImagesSet()))
                .tagsDtos(TagsDto.returnTagsDtoSet(events.getTagsSet()))
                .accountsDtos(AccountsDto.returnAccountsDtoSet(events.getAccountsEventsSet()))
                .build();
    }


    public static Set<EventsDto> returnEventsDtoSet(Set<AccountsMapperEvents> accountsMapperEvents) {
        if (accountsMapperEvents == null) {
            return new HashSet<>();
        }
        return accountsMapperEvents.stream()
                        .map(EventsDto::from)
                        .collect(Collectors.toSet());

    }


}
