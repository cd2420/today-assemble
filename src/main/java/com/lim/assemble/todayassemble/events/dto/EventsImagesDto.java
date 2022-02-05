package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.events.entity.EventsImages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsImagesDto {

    @NotNull
    private ImagesType imagesType;

    private String image;

    private static EventsImagesDto from(EventsImages eventsImages) {

        return new EventsImagesDto(eventsImages.getImagesType(), eventsImages.getImage());
    }

    public static Set<EventsImagesDto> returnEventsImagesDtoSet(Set<EventsImages> eventsImagesSet) {
        if (eventsImagesSet == null) {
            return new HashSet<>();
        }
        return eventsImagesSet.stream()
                            .map(EventsImagesDto::from)
                            .collect(Collectors.toSet());
    }

}
