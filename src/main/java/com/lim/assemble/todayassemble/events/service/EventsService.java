package com.lim.assemble.todayassemble.events.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsReq;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsTagsReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsService {

    List<EventsDto> getEventsList(Pageable pageable);

    EventsDto getEvents(Long eventId);

    EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts);

    EventsDto updateEvents(UpdateEventsReq updateEventsReq, Accounts accounts);

    EventsDto updateEventsTags(UpdateEventsTagsReq updateEventsReq, Accounts accounts);
}
