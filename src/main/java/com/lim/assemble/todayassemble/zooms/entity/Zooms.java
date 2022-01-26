package com.lim.assemble.todayassemble.zooms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "zooms")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Zooms extends BaseEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "events_id")
    @JsonManagedReference
    private Events events;

    private String url;

    private Boolean status;

    public static Zooms from(ZoomsDto zoomsDto) {
        return Zooms.builder()
                .url(zoomsDto.getUrl())
                .status(zoomsDto.getStatus())
                .build();
    }

    public static Set<Zooms> returnZoomsSetFrom(CreateEventsReq createEventsReq) {
        Set<ZoomsDto> zoomsSet = createEventsReq.getZoomsSet();

        if (zoomsSet == null) {
            return new HashSet<>();
        } else {
            return zoomsSet.stream()
                    .map(Zooms::from)
                    .collect(Collectors.toSet());
        }
    }

}
