package com.lim.assemble.todayassemble.likes.dto;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesEventsDto {

    private EventsDto eventsDto;

    public static Set<LikesEventsDto> returnLikesDtoSet(Set<Likes> likesSet) {
        if (likesSet == null) {
            return new HashSet<>();
        }

        return likesSet.stream()
                        .map((likesDto) -> {
                            return new LikesEventsDto(
                                    EventsDto.builder()
                                    .id(likesDto.getEvents().getId())
                                    .build()
                            );
                        })
                        .collect(Collectors.toSet());
    }


}
