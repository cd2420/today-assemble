package com.lim.assemble.todayassemble.domain.entity.base;


import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@MappedSuperclass
@Getter
@NoArgsConstructor
public abstract class Images extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ImagesType imagesType;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String image;

    public Images(ImagesType imagesType, String image) {
        this.imagesType = imagesType;
        this.image = image;
    }
}
