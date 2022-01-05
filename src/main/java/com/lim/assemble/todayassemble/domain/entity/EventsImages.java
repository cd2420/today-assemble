package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.Images;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "events_images")
@Getter
@SuperBuilder
public class EventsImages extends Images {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id")
    private Events events;
}
