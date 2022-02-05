package com.lim.assemble.todayassemble.accounts.config;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsImagesDto;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CreateEventsReqFactory {

    public static CreateEventsReq getCreateEventsReq(
            EventsType eventsType
            , Set<EventsImagesDto> eventsImagesDtos
            , Set<TagsDto> tagsDtos
    ) {
        CreateEventsReq createEventsReq = null;
        if (EventsType.OFFLINE.equals(eventsType)) {
            createEventsReq = CreateEventsReq.builder()
                    .name("영화모임")
                    .description("스파이더맨 영화")
                    .maxMembers(10)
                    .eventsType(eventsType)
                    .eventsTime(LocalDateTime.now())
                    .takeTime(2L)
                    .eventsImagesSet(eventsImagesDtos)
                    .tagsSet(tagsDtos)
                    .address("서울특별시 강남구 강남대로 438 스타플렉스")
                    .longitude("37.501646450019")
                    .latitude("127.0262170654")
                    .build();
        } else {

            ZoomsDto zoomsDto = ZoomsDto.builder()
                    .status(true)
                    .url("www.~~~~~~")
                    .build();
            Set<ZoomsDto> zoomsDtos = new HashSet<>();
            zoomsDtos.add(zoomsDto);

            createEventsReq = CreateEventsReq.builder()
                    .name("영화모임")
                    .description("스파이더맨 영화")
                    .maxMembers(10)
                    .eventsType(eventsType)
                    .eventsTime(LocalDateTime.now())
                    .takeTime(2L)
                    .eventsImagesSet(eventsImagesDtos)
                    .tagsSet(tagsDtos)
                    .zoomsSet(zoomsDtos)
                    .build();
        }

        return createEventsReq;
    }

}
