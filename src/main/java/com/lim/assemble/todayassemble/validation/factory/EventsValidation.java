package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventsValidation implements Validation {

    private final EventsRepository eventsRepository;

    private ValidateType validateType = ValidateType.EVENT;

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target) {

        if (Events.class.equals(target.getClass())) {
            // 생성 validate
            createValidate((Events) target);
        } else if (UpdateEventsDto.class.equals(target.getClass())) {
            // 수정 validate
            updateValidate((UpdateEventsDto) target);
        } else {
            updateTagsOrImagesValidate((UpdateEventsReqBase) target);
        }
    }

    private void updateTagsOrImagesValidate(UpdateEventsReqBase target) {

        // events가 존재하는지 체크.
        Events events = checkExistEvents(target.getId());

        // 해당 event 주인이 본인이 맞는지 체크.
        validateEventsHost(target.getAccountsId(), events.getAccounts().getId());
    }

    private void updateValidate(UpdateEventsDto target) {
        UpdateEventsReq updateEventsReq = (UpdateEventsReq) target.getUpdateEventsReq();

        // events가 존재하는지 체크.
        checkExistEvents(updateEventsReq.getId());

        Events events = Events.from(updateEventsReq, target.getAccounts());
        events.setId(updateEventsReq.getId());

        // 기존에 있는 event 시간이랑 겹치는 체크.
        validateEventsTime(events);

        // 해당 event 주인이 본인이 맞는지 체크.
        validateEventsHost(target.getAccounts().getId(), target.getUpdateEventsReq().getAccountsId());
    }

    private Events checkExistEvents(Long eventsId) {
        return eventsRepository.findById(eventsId)
                .orElseThrow(() -> {
                    throw new TodayAssembleException(ErrorCode.NO_EVENTS_ID);
                });
    }

    private void validateEventsHost(Long accountsId, Long eventsAccountsId) {

        if (!accountsId.equals(eventsAccountsId)) {
            throw new TodayAssembleException(ErrorCode.NOT_EQUAL_ACCOUNT);
        }

    }

    private void createValidate(Events target) {
        // 기존에 있는 event 시간이랑 겹치는 체크.
        validateEventsTime(target);
    }

    private void validateEventsTime(Events target) {
        Accounts accounts = target.getAccounts();
        List<Events> eventsList = eventsRepository.findByAccountsId(accounts.getId());

        if (eventsList == null) {
            return;
        } else {
            eventsList.stream()
                    .forEach(item -> {
                        if (!checkEventTime(item, target) && !item.getId().equals(target.getId())) {
                            throw new TodayAssembleException(ErrorCode.DATE_DUPLICATE);
                        };
                    });
        }
    }

    private boolean checkEventTime(Events checkEvents, Events events) {
        LocalDateTime eventsStartTime = events.getEventsTime();
        LocalDateTime eventsEndTime = eventsStartTime.plusHours(events.getTakeTime());

        long eventsStartTimeMillSec = getLocalDateTimeToMillSec(eventsStartTime);
        long eventsEndTimeMillSec = getLocalDateTimeToMillSec(eventsEndTime);

        LocalDateTime checkStartTime = checkEvents.getEventsTime();
        LocalDateTime checkEndTime = checkStartTime.plusHours(checkEvents.getTakeTime());

        long checkStartTimeMillSec = getLocalDateTimeToMillSec(checkStartTime);
        long checkEndTimeMillSec = getLocalDateTimeToMillSec(checkEndTime);

        if ((eventsStartTimeMillSec >= checkStartTimeMillSec && eventsStartTimeMillSec <= checkEndTimeMillSec)
            || (eventsEndTimeMillSec >= checkStartTimeMillSec && eventsEndTimeMillSec <= checkEndTimeMillSec)) {
            return false;
        }
        return true;

    }

    private long getLocalDateTimeToMillSec(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }
}
