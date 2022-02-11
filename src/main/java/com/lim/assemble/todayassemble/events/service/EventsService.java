package com.lim.assemble.todayassemble.events.service;

import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
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

    AccountsEventsDto<Accounts>  participateEventsManage(Long eventsId, Accounts accounts);

//    Object inviteEvents(Long eventsId, Accounts accounts, Long accountsId);
}
