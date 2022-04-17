package com.lim.assemble.todayassemble.events.repository;

import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.QEvents;
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
    public PageImpl<Events> getEventsList(Pageable pageable) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.hostAccountsId.eq(events.accounts.id))
                .orderBy(events.eventsTime.asc());
        long totalCount = query.fetchCount();
        List<Events> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public PageImpl<Events> getEventsList(Pageable pageable, LocalDateTime localDateTime) {
        JPAQueryFactory factory = new JPAQueryFactory(entityManager);
        QEvents events = QEvents.events;

        JPQLQuery<Events> query = jpaQueryFactory
                .selectFrom(events)
                .where(events.hostAccountsId.eq(events.accounts.id)
                        .and(events.eventsTime.after(localDateTime))
                )
                .orderBy(events.eventsTime.asc());
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
}
