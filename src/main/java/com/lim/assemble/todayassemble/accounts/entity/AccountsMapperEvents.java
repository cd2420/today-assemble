package com.lim.assemble.todayassemble.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "accounts_mapper_events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountsMapperEvents extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    @JsonIgnore
    private Accounts accounts;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "events_id", nullable = false)
    @JsonIgnore
    private Events events;

    private Boolean isParticipating;
}
