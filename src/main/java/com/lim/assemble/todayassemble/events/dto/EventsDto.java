package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventsDto {

    private String name;

    public static EventsDto from(Events events) {
        return EventsDto.builder()
                .name(events.getName())
                .build();
    }
}
