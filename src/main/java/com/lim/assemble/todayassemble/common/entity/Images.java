package com.lim.assemble.todayassemble.common.entity;


import com.lim.assemble.todayassemble.common.type.ImagesType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class Images extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ImagesType imagesType;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

    public Images(ImagesType imagesType, String image) {
        this.imagesType = imagesType;
        this.image = image;
    }
}
