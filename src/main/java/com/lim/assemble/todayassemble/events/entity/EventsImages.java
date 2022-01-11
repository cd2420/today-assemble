package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.common.entity.Images;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "events_images")
@Getter
public class EventsImages extends Images {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id")
    @JsonManagedReference
    private Events events;

    @Builder
    public EventsImages(
            ImagesType imagesType
            , String image
            , Events events) {

        super(imagesType, image);
        this.events = events;
    }

    public EventsImages() {
        super();
    }
}
