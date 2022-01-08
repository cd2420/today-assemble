package com.lim.assemble.todayassemble.events.entity;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.places.entity.Places;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import com.lim.assemble.todayassemble.common.type.EventsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Events extends BaseEntity {

    private String name;

    private String host_email;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    private Accounts accounts;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "places_id", nullable = true)
    private Places places;

    private Integer maxMembers;

    private Integer likes;

    @Enumerated(EnumType.STRING)
    private EventsType eventsType;

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<EventsImages> eventsImagesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Likes> likesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Tags> tagsSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Zooms> zoomsSet = new HashSet<>();

}
