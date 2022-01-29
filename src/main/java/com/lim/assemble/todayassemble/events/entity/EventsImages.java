package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.common.entity.Images;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsImagesDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "events_images")
@Getter
public class EventsImages extends Images {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id")
    @JsonIgnore
    private Events events;

    @Builder
    public EventsImages(
            ImagesType imagesType
            , String image
            , Events events) {

        super(imagesType, image);
        this.events = events;
    }

    @Builder
    public EventsImages(
            ImagesType imagesType
            , String image
            ) {

        super(imagesType, image);
    }

    public EventsImages() {
        super();
    }

    public static EventsImages from(
            EventsImagesDto eventsImagesDto
    ) {

        return EventsImages.builder()
                .imagesType(eventsImagesDto.getImagesType())
                .image(eventsImagesDto.getImage())
                .build();
    }

    public static EventsImages of(
            EventsImagesDto eventsImagesDto
            , Events events
    ) {

        return EventsImages.builder()
                .imagesType(eventsImagesDto.getImagesType())
                .image(eventsImagesDto.getImage())
                .events(events)
                .build();
    }

    public static Set<EventsImages> returnImagesSetFrom(CreateEventsReq createEventsReq) {
        Set<EventsImagesDto> eventsImagesSet = createEventsReq.getEventsImagesSet();

        if (eventsImagesSet == null) {
            return new HashSet<>();
        } else {
            return eventsImagesSet.stream()
                        .map(EventsImages::from)
                        .collect(Collectors.toSet());
        }

    }
}
