package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsContentsReq;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.tags.entity.Tags;
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

    @Lob @Basic(fetch = FetchType.EAGER)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_id", nullable = false)
    @JsonBackReference
    private Accounts accounts;

    private Integer maxMembers;

    @Enumerated(EnumType.STRING)
    private EventsType eventsType;

    private LocalDateTime eventsTime;

    private Long takeTime;

    private String address;

    private String longitude;

    private String latitude;

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<EventsImages> eventsImagesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Likes> likesSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<AccountsMapperEvents> accountsEventsSet = new HashSet<>();

    @OneToMany(mappedBy = "events", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Tags> tagsSet = new HashSet<>();

    public static Events of(CreateEventsReq createEventsReq, Accounts accounts) {

        Events events = Events.builder()
                .name(createEventsReq.getName())
                .description(createEventsReq.getDescription())
                .accounts(accounts)
                .maxMembers(createEventsReq.getMaxMembers())
                .eventsType(createEventsReq.getEventsType())
                .eventsTime(createEventsReq.getEventsTime())
                .takeTime(createEventsReq.getTakeTime())
                .address(createEventsReq.getAddress())
                .longitude(createEventsReq.getLongitude())
                .latitude(createEventsReq.getLatitude())
                .likesSet(new HashSet<>())
                .accountsEventsSet(new HashSet<>())
                .eventsImagesSet(EventsImages.returnImagesSetFrom(createEventsReq))
                .tagsSet(Tags.returnTagsSetFrom(createEventsReq))
                .build();

        events.getEventsImagesSet()
                .forEach(eventsImages -> eventsImages.setEvents(events));

        events.getTagsSet()
                .forEach(tags -> tags.setEvents(events));

        return events;
    }

    public static Events of(UpdateEventsContentsReq updateEventsContentsReq) {

        Events events = Events.builder()
                .eventsTime(updateEventsContentsReq.getEventsTime())
                .takeTime(updateEventsContentsReq.getTakeTime())
                .build();

        return events;
    }

    public void update(UpdateEventsContentsReq updateEventsContentsReq) {
        this.name = updateEventsContentsReq.getName();
        this.description = updateEventsContentsReq.getDescription();
        this.maxMembers = updateEventsContentsReq.getMaxMembers();
        this.eventsTime = updateEventsContentsReq.getEventsTime();
        this.takeTime = updateEventsContentsReq.getTakeTime();
        this.address = updateEventsContentsReq.getAddress();
        this.latitude = updateEventsContentsReq.getLatitude();
        this.longitude = updateEventsContentsReq.getLongitude();
    }
}
