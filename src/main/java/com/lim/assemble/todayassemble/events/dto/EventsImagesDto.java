package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.Data;

import javax.persistence.*;

@Data
public class EventsImagesDto {

    private ImagesType imagesType;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;
}
