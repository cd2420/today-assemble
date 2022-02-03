package com.lim.assemble.todayassemble.likes.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.likes.repository.LikesRepository;
import com.lim.assemble.todayassemble.likes.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService {

    private final LikesRepository likesRepository;
    private final EventsRepository eventsRepository;

    @Override
    public void manageLikes(Long eventsId, Accounts accounts) {

        Optional<Likes> likesOptional = likesRepository.findByAccountsIdAndEventsId(accounts.getId(), eventsId);
        Events events = eventsRepository.findById(eventsId)
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID)
                );
        // 1. 좋아요가 있을 경우 -> 좋아요 삭제
        if (likesOptional.isPresent()) {
            Likes likes = likesOptional.get();
            removeLikes(likes, accounts.getLikesSet());
            removeLikes(likes, events.getLikesSet());
        }
        else {
            // 2. 좋아요가 없을 경우 -> 좋아요 생성
            Likes likes = new Likes();
            likes.setAccounts(accounts);
            likes.setEvents(events);

            createLikes(likes, events.getLikesSet());
            createLikes(likes, accounts.getLikesSet());

        }

    }

    public void removeLikes(Likes likes, Set<Likes> likesSet) {
        Likes deleteAccountsLikes = likesSet.stream()
                .filter(item -> item.getId().equals(likes.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new TodayAssembleException(ErrorCode.NO_LIKES_ID)
                );
        likesSet.remove(deleteAccountsLikes);
    }

    public void createLikes(Likes likes, Set<Likes> likesSet) {
        if (likesSet == null) {
            likesSet = new HashSet<>();
        }
        likesSet.add(likes);
    }

}
