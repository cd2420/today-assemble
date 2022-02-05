package com.lim.assemble.todayassemble.zooms.dto;

import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class ZoomsDto {

    private String url;

    private Boolean status;

    public static ZoomsDto from(Zooms zooms) {
        return ZoomsDto.builder()
                .url(zooms.getUrl())
                .status(zooms.getStatus())
                .build();
    }

    public static Set<ZoomsDto> returnZoomsDtoSet(Set<Zooms> zoomsSet) {
        if (zoomsSet == null) {
            return new HashSet<>();
        }

        return zoomsSet.stream()
                        .map(ZoomsDto::from)
                        .collect(Collectors.toSet());
    }
}
