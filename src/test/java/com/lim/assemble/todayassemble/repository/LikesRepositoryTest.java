package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import com.lim.assemble.todayassemble.likes.repository.LikesRepository;
import com.lim.assemble.todayassemble.places.entity.Places;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.places.repository.PlacesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("기본 좋아요 > 기본 좋아요 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        Likes likes = (Likes) EntityFactory.createEntity(Entity_Type.LIKES);
        Accounts accounts = likes.getAccounts();
        Events events = likes.getEvents();
        Places places = events.getPlaces();

        // when
        accountsRepository.save(accounts);
        placesRepository.save(places);
        eventsRepository.save(events);
        likesRepository.save(likes);

        entityManager.clear();

        // then
        List<Likes> likesList = likesRepository.findAll();

        Likes check = likesList.get(0);
        assertEquals(check.getAccounts().getName(), accounts.getName());
        assertEquals(check.getEvents().getName(), events.getName());

    }
}