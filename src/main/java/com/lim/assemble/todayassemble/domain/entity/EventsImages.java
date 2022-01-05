package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.BaseEntity;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "events_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventsImages extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ImagesType imagesType;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id")
    private Events events;
}