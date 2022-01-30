package com.lim.assemble.todayassemble.events.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsService {

    List<EventsDto> getEventsList(Pageable pageable);

    EventsDto getEvents(Long eventId);

    EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts);

    EventsDto updateEvents(UpdateEventsReqBase updateEventsReqBase, Accounts accounts);

    void deleteEvents(Long eventsId, Accounts accounts);
}
