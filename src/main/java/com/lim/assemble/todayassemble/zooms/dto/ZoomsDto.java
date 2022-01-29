package com.lim.assemble.todayassemble.zooms.dto;

import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.Builder;
import lombok.Data;

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
}
