package com.lim.assemble.todayassemble.tags.dto;

import com.lim.assemble.todayassemble.tags.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsDto {

    private String name;

    public static TagsDto from(Tags tags) {
        return new TagsDto(tags.getName());
    }

    public static Set<TagsDto> returnTagsDtoSet(Set<Tags> tagsSet) {
        if (tagsSet == null) {
            return new HashSet<>();
        }
        return tagsSet.stream()
                    .map(TagsDto::from)
                    .collect(Collectors.toSet());
    }

}
