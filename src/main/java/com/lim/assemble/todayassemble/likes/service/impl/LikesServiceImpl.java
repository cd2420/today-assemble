package com.lim.assemble.todayassemble.likes.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.likes.repository.LikesRepository;
import com.lim.assemble.todayassemble.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final EventsRepository eventsRepository;

    @Override
    public EventsDto manageLikes(Long eventsId, Accounts accounts) {

        Optional<Likes> likesOptional = likesRepository.findByAccountsIdAndEventsId(accounts.getId(), eventsId);
        Events events = eventsRepository.findById(eventsId)
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID)
                );
        // 1. 좋아요가 있을 경우 -> 좋아요 삭제
        if (likesOptional.isPresent()) {
            removeLikes(likesOptional.get(), accounts, events);
        } else {
            // 2. 좋아요가 없을 경우 -> 좋아요 생성
            createLikes(accounts, events);

        }

        return EventsDto.from(events);
    }

    public void removeLikes(Likes likes, Accounts accounts, Events events) {
        deleteLikesFromSet(likes, accounts.getLikesSet());
        deleteLikesFromSet(likes, events.getLikesSet());
    }

    public void deleteLikesFromSet(Likes likes, Set<Likes> likesSet) {
        Likes deleteAccountsLikes = likesSet.stream()
                .filter(item -> item.getId().equals(likes.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_LIKES_ID)
                );
        likesSet.remove(deleteAccountsLikes);
    }

    public void createLikes(Accounts accounts, Events events) {
        Likes likes = Likes.builder()
                .accounts(accounts)
                .events(events)
                .build();

        addLikesToSet(likes, events.getLikesSet());
        addLikesToSet(likes, accounts.getLikesSet());
    }

    public void addLikesToSet(Likes likes, Set<Likes> likesSet) {
        if (likesSet == null) {
            likesSet = new HashSet<>();
        }
        likesSet.add(likes);
    }

    @Override
    public List<EventsDto> getAccountLikesEventList(Accounts accounts) {
        Set<Likes> likesSet = accounts.getLikesSet();
        if (likesSet == null) {
            return new ArrayList<>();
        } else {
            return likesSet.stream()
                    .map(like -> EventsDto.from(like.getEvents()))
                    .collect(Collectors.toList());
        }
    }

}
