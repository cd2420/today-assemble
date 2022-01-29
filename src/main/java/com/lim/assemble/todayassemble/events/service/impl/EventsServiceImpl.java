package com.lim.assemble.todayassemble.events.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final ValidationFactory validationFactory;
    private final EventsRepository eventsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> getEventsList(Pageable pageable) {
        return eventsRepository.findAll(pageable).stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EventsDto getEvents(Long eventId) {
        return EventsDto.from(
                eventsRepository.findById(eventId)
                        .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID))
        );
    }

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
    @Transactional
    public EventsDto updateEvents(UpdateEventsReq updateEventsReq, Accounts accounts) {
        // updateEventsReq validation check : { 호스트가 수정할려는 모임이 기존에 자기가 만든 모임과 시간이 겹치는지 체크, 호스트가 맞는지 체크}
        UpdateEventsDto updateEventsDto = new UpdateEventsDto(updateEventsReq, accounts);
        validationFactory.createValidation(ValidateType.EVENT).validate(updateEventsDto);

        // events 수정
        Events events = eventsRepository.getById(updateEventsReq.getId());
        events.update(updateEventsReq);

        return EventsDto.from(events);
    }

    @Override
    @Transactional
    public EventsDto updateEventsTags(UpdateEventsTagsReq updateEventsTagsReq, Accounts accounts) {

        // updateEventsReq validation check : {호스트가 맞는지 체크}
        UpdateEventsTagsDto updateEventsTagsDto = new UpdateEventsTagsDto(updateEventsTagsReq, accounts);
        validationFactory.createValidation(ValidateType.EVENT).validate(updateEventsTagsDto);

        // evets tag 수정
        Events events = eventsRepository.getById(updateEventsTagsReq.getId());
        if (events.getTagsSet() != null) {
            events.getTagsSet().clear();
        } else {
            events.setTagsSet(new HashSet<>());
        }
        events.getTagsSet().addAll(
                updateEventsTagsReq.getTags().stream()
                    .map(tags -> Tags.of(tags, events))
                    .collect(Collectors.toSet())
        );

        return EventsDto.from(events);
    }
}
