package com.lim.assemble.todayassemble.events.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.entity.BaseEntity;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.likes.entity.Likes;
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
    @JsonManagedReference
    private Accounts accounts;

    private Integer maxMembers;

    private Integer likes;

    @Enumerated(EnumType.STRING)
    private EventsType eventsType;

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

    public static Events from(CreateEventsReq createEventsReq, Accounts accounts) {
        return Events.builder()
                .name(createEventsReq.getName())
                .host_email(accounts.getEmail())
                .description(createEventsReq.getDescription())
                .accounts(accounts)
                .maxMembers(createEventsReq.getMaxMembers())
                .likes(0)
                .eventsType(createEventsReq.getEventsType())
//                .eventsImagesSet(
//                        createEventsReq.getEventsImagesSet() == null
//                                ? new HashSet<>() : createEventsReq.getEventsImagesSet()
//                )
//                .tagsSet(
//                        createEventsReq.getTagsSet() == null
//                            ? new HashSet<>() : createEventsReq.getTagsSet()
//                )
//                .zoomsSet(
//                        createEventsReq.getZoomsSet() == null
//                            ? new HashSet<>() : createEventsReq.getZoomsSet()
//                )
                .build();

    }

}
