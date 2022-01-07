package com.lim.assemble.todayassemble.places.entity;

import com.lim.assemble.todayassemble.common.entity.Images;
import com.lim.assemble.todayassemble.type.ImagesType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "places_images")
@Getter
public class PlacesImages extends Images {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "places_id")
    private Places places;

    @Builder
    public PlacesImages(
            ImagesType imagesType
            , String image
            , Places places) {

        super(imagesType, image);
        this.places = places;
    }

    public PlacesImages() {
        super();
    }
}
