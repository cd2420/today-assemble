package com.lim.assemble.todayassemble.likes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "accounts_like_events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Likes extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    @JsonBackReference
    private Accounts accounts;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id", nullable = false)
    @JsonBackReference
    private Events events;


}
