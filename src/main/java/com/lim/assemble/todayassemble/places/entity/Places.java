package com.lim.assemble.todayassemble.places.entity;

import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.entity.Events;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "places")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Places extends BaseEntity {

    private String name;

    private String address;

    private String latitude;

    private String longtitude;

    @OneToMany(mappedBy = "places", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Events> eventsSet = new HashSet<>();

    @OneToMany(mappedBy = "places", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<PlacesImages> placesImagesSet = new HashSet<>();

}
