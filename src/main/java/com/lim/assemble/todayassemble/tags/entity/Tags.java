package com.lim.assemble.todayassemble.tags.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tags")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tags extends BaseEntity {

    @Setter
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "events_id")
    @JsonManagedReference
    private Events events;

    private String name;

    public static Tags from(TagsDto tagsDto) {
        return Tags.builder()
                .name(tagsDto.getName())
                .build();
    }

    public static Set<Tags> returnTagsSetFrom(CreateEventsReq createEventsReq) {
        Set<TagsDto> tagsSet = createEventsReq.getTagsSet();

        if (tagsSet == null) {
            return new HashSet<>();
        } else {
            return tagsSet.stream()
                    .map(Tags::from)
                    .collect(Collectors.toSet());
        }
    }


}
