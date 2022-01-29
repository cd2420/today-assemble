package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsReq;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Events extends BaseEntity {

    private String name;

    private String hostName;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accounts_id", nullable = false)
    @JsonIgnore
    private Accounts accounts;

    private Integer maxMembers;

    private Integer likes;

    @Enumerated(EnumType.STRING)
    private EventsType eventsType;

    private LocalDateTime eventsTime;

    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonBackReference
    private Set<EventsImages> eventsImagesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Likes> likesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Tags> tagsSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Zooms> zoomsSet = new HashSet<>();

    // TODO 장소 데이터

    public static Events from(CreateEventsReq createEventsReq, Accounts accounts) {

        Events events = Events.builder()
                .name(createEventsReq.getName())
                .hostName(accounts.getName())
                .description(createEventsReq.getDescription())
                .accounts(accounts)
                .maxMembers(createEventsReq.getMaxMembers())
                .likes(0)
                .eventsType(createEventsReq.getEventsType())
                .eventsTime(createEventsReq.getEventsTime())
                .takeTime(createEventsReq.getTakeTime())
                .address(createEventsReq.getAddress())
                .longitude(createEventsReq.getLongitude())
                .latitude(createEventsReq.getLatitude())
                .eventsImagesSet(EventsImages.returnImagesSetFrom(createEventsReq))
                .tagsSet(Tags.returnTagsSetFrom(createEventsReq))
                .zoomsSet(Zooms.returnZoomsSetFrom(createEventsReq))
                .build();

        events.getEventsImagesSet().stream()
                .forEach(eventsImages -> eventsImages.setEvents(events));

        events.getTagsSet().stream()
                .forEach(tags -> tags.setEvents(events));

        events.getZoomsSet().stream()
                .forEach(zooms -> zooms.setEvents(events));

        return events;
    }

    public static Events from(UpdateEventsReq updateEventsReq, Accounts accounts) {

        Events events = Events.builder()
                .name(updateEventsReq.getName())
                .hostName(accounts.getName())
                .description(updateEventsReq.getDescription())
                .accounts(accounts)
                .maxMembers(updateEventsReq.getMaxMembers())
                .eventsTime(updateEventsReq.getEventsTime())
                .takeTime(updateEventsReq.getTakeTime())
                .address(updateEventsReq.getAddress())
                .longitude(updateEventsReq.getLongitude())
                .latitude(updateEventsReq.getLatitude())
                .build();

        return events;
    }

    public void update(UpdateEventsReq updateEventsReq) {
        this.name = updateEventsReq.getName();
        this.description = updateEventsReq.getDescription();
        this.maxMembers = updateEventsReq.getMaxMembers();
        this.eventsTime = updateEventsReq.getEventsTime();
        this.takeTime = updateEventsReq.getTakeTime();
        this.address = updateEventsReq.getAddress();
        this.longitude = updateEventsReq.getLongitude();
        this.latitude = updateEventsReq.getLatitude();
    }
}
