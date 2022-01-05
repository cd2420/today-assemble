package com.lim.assemble.todayassemble.domain.entity.base;

import com.lim.assemble.todayassemble.domain.entity.base.BaseEntity;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@MappedSuperclass
@SuperBuilder
public abstract class Images extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ImagesType imagesType;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String image;

}
