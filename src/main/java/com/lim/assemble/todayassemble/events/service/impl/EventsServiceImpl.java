package com.lim.assemble.todayassemble.events.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final ValidationFactory validationFactory;
    private final EventsRepository eventsRepository;


    @Override
    @Transactional
    public EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts) {

        // createEventsReq validation check : { 호스트가 생성할려는 모임이 기존에 자기가 만든 모임과 시간이 겹치는지 체크 }
        Events events = Events.from(createEventsReq, accounts);
        validationFactory.createValidation(ValidateType.EVENT).validate(events);

        // events 생성
        return EventsDto.from(
                eventsRepository.save(events)
        );
    }

    @Override
    public List<EventsDto> getEvents() {
        return eventsRepository.findAll().stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }
}
