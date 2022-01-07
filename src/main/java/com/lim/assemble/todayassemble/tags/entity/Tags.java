package com.lim.assemble.todayassemble.tags.entity;

import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tags extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "events_id")
    private Events events;

    private String name;
}
