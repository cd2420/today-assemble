package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.*;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagsRepositoryTest {

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("기본 태그 > 기본 태그 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {

        // given
        Tags tags = (Tags) EntityFactory.createEntity(Entity_Type.TAGS);
        Events events = tags.getEvents();
        Accounts accounts = events.getAccounts();
        Places places = events.getPlaces();

        // when
        accountsRepository.save(accounts);
        placesRepository.save(places);
        eventsRepository.save(events);
        tagsRepository.save(tags);

        entityManager.clear();

        // then
        List<Tags> tagsList = tagsRepository.findAll();

        Tags check = tagsList.get(0);
        assertNotNull(check);
        assertEquals(check.getEvents().getName(), events.getName());

    }

}