package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.accounts.entity.QAccounts;
import com.lim.assemble.todayassemble.accounts.entity.QAccountsMapperEvents;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.QEvents;
import com.lim.assemble.todayassemble.events.entity.QEventsImages;
import com.lim.assemble.todayassemble.likes.entity.QLikes;
import com.lim.assemble.todayassemble.tags.entity.QTags;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.annotation.Generated;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Generated("com.lim.assemble.todayassemble.events.entity.Events")
public class EventsCustomRepositoryImpl extends QuerydslRepositorySupport implements EventsCustomRepository {


    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    public EventsCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory, EntityManager entityManager) {
        super(Events.class);
        this.jpaQueryFactory = jpaQueryFactory;
        this.entityManager = entityManager;
    }

    @Override
    public PageImpl<Events> getEventsList(Pageable pageable, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.accounts.id.eq(events.accounts.id)
                        .and(events.eventsTime.after(localDateTime))
                )
                .leftJoin(events.eventsImagesSet, QEventsImages.eventsImages).fetchJoin()
                .leftJoin(events.likesSet, QLikes.likes).fetchJoin()
                .leftJoin(events.accountsEventsSet, QAccountsMapperEvents.accountsMapperEvents).fetchJoin()
                .leftJoin(events.tagsSet, QTags.tags).fetchJoin()
                .leftJoin(events.accounts, QAccounts.accounts).fetchJoin()
                .orderBy(events.eventsTime.asc())
                .distinct()
                ;
        long totalCount = query.fetchCount();
        List<Events> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public PageImpl<Events> findByKeyword(Pageable pageable, String keyword, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.eventsTime.after(localDateTime)
                        .and(events.name.containsIgnoreCase(keyword))
                        .or(events.tagsSet.any().name.eq(keyword)))
                .leftJoin(events.tagsSet, QTags.tags).fetchJoin()
                .orderBy(events.eventsTime.asc())
                .distinct();

        long totalCount = query.fetchCount();
        List<Events> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Integer findByKeywordSize(String keyword, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.eventsTime.after(localDateTime)
                        .and(events.name.containsIgnoreCase(keyword))
                        .or(events.tagsSet.any().name.eq(keyword)))
                .leftJoin(events.tagsSet, QTags.tags).fetchJoin()
                .orderBy(events.eventsTime.asc())
                .distinct();

        long totalCount = query.fetchCount();
        return Math.toIntExact(totalCount);
    }

    @Override
    public PageImpl<Events> findByPlace(Pageable pageable, String keyword, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.eventsTime.after(localDateTime)
                        .and(events.address.containsIgnoreCase(keyword))
                        )
                .leftJoin(events.tagsSet, QTags.tags).fetchJoin()
                .orderBy(events.eventsTime.asc())
                .distinct();

        long totalCount = query.fetchCount();
        List<Events> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Integer findByPlaceSize(String keyword, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.eventsTime.after(localDateTime)
                        .and(events.address.containsIgnoreCase(keyword))
                )
                .leftJoin(events.tagsSet, QTags.tags).fetchJoin()
                .orderBy(events.eventsTime.asc())
                .distinct();

        long totalCount = query.fetchCount();
        return Math.toIntExact(totalCount);
    }
}
