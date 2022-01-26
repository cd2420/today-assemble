package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @JsonManagedReference
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

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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
                .host_email(accounts.getEmail())
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

}
