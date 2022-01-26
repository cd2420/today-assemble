package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
    public void validate(Object target) {
        Events events = (Events) target;
        Accounts accounts = events.getAccounts();
        List<Events> eventsList = eventsRepository.findByAccountsId(accounts.getId());

        if (eventsList == null) {
            return;
        } else {
            eventsList.stream()
                    .forEach(item -> {
                        if (!checkEventTime(item, events)) {
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
