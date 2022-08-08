package com.lim.assemble.todayassemble.events.service;

import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsReqBase;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventsService {

    List<EventsDto> getEventsList(Pageable pageable);

    EventsDto getEvents(Long eventId);

    Integer getParticipateEventsAccountsSize(Long eventId);

    EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts);

    EventsDto updateEvents(UpdateEventsReqBase updateEventsReqBase, Accounts accounts);

    void deleteEvents(Long eventsId, Accounts accounts);

    AccountsEventsDto<EventsDto> participateEventsManage(Long eventsId, Accounts accounts);

//    AccountsEventsDto<EventsDto> inviteEvents(Long eventsId, Accounts accounts, Long inviteAccountsId);
//
//    AccountsEventsDto<AccountsDto> responseEventsInvite(
//            Long eventsId
//            , Accounts accounts
//            , UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq
//    );

    Integer getEventsListSize();

    List<EventsDto> searchEventsList(String target, Pageable pageable);

    Integer searchEventsSize(String keyword);

    List<EventsDto> searchEventsListByPlace(String keyword, Pageable pageable);

    Integer searchEventsSizeByPlace(String keyword);
}
