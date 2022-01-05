package com.lim.assemble.todayassemble.domain.entity;

import com.lim.assemble.todayassemble.domain.entity.base.Images;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "places_images")
@Getter
@SuperBuilder
public class PlacesImages extends Images {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "places_id")
    private Places places;
}
