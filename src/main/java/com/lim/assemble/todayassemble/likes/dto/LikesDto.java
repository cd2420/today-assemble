package com.lim.assemble.todayassemble.likes.dto;

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
public class LikesDto {

    private EventsDto eventsDto;

    public static LikesDto from(Likes likes) {
        return new LikesDto(EventsDto.from(likes.getEvents()));
    }

    public static Set<LikesDto> returnLikesDtoSet(Set<Likes> likesSet) {
        if (likesSet == null) {
            return new HashSet<>();
        }

        return likesSet.stream()
                        .map(LikesDto::from)
                        .collect(Collectors.toSet());
    }


}
