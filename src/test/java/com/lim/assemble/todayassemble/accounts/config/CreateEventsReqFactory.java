package com.lim.assemble.todayassemble.accounts.config;

import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsImagesDto;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;

import java.time.LocalDateTime;
import java.util.Set;

public class CreateEventsReqFactory {

    public static CreateEventsReq getCreateEventsReq(
            EventsType eventsType
            , Set<EventsImagesDto> eventsImagesDtos
            , Set<TagsDto> tagsDtos
    ) {
        CreateEventsReq createEventsReq = new CreateEventsReq();
        createEventsReq.setName("영화모임");
        createEventsReq.setDescription("스파이더맨 영화");
        createEventsReq.setMaxMembers(10);
        createEventsReq.setEventsType(EventsType.OFFLINE);
        createEventsReq.setEventsTime(LocalDateTime.now());
        createEventsReq.setTakeTime(2L);
        createEventsReq.setEventsImagesSet(eventsImagesDtos);
        createEventsReq.setTagsSet(tagsDtos);
        createEventsReq.setAddress("서울특별시 강남구 강남대로 438 스타플렉스");
        createEventsReq.setLongitude("37.501646450019");
        createEventsReq.setLatitude("127.0262170654");

        return createEventsReq;
    }

}
