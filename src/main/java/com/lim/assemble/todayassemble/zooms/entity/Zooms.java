package com.lim.assemble.todayassemble.zooms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "zooms")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Zooms extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "events_id")
    @JsonManagedReference
    private Events events;

    private String url;

    private Boolean status;

}
