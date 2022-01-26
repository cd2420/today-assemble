package com.lim.assemble.todayassemble.events.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;

import java.util.List;

public interface EventsService {
    EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts);

    List<EventsDto> getEvents();
}
