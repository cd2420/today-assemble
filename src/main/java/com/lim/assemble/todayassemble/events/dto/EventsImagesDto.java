package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
public class EventsImagesDto {

    @NotNull
    private ImagesType imagesType;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;
}
