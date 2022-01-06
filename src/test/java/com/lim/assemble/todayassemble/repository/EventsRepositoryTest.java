package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.Events;
import com.lim.assemble.todayassemble.domain.entity.Places;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class EventsRepositoryTest {

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("기본 이벤트 저장 > 계정과 장소에 맵핑되는지 확인")
    @Transactional
    void givenBasicData_whenSaveRepository_thenCheckSettingAccountsEventsAndPlacesEvents() {
        //given
        Events events = (Events) EntityFactory.createEntity(Entity_Type.EVENTS);
        Places places = events.getPlaces();
        Accounts accounts = events.getAccounts();

        placesRepository.save(places);
        accountsRepository.save(accounts);
        eventsRepository.save(events);

        entityManager.clear();

        // when
        List<Events> eventsList = eventsRepository.findAll();
        List<Places> placesList = placesRepository.findAll();
        List<Accounts> accountsList = accountsRepository.findAll();

        // then
        Iterator<Events> itEvents = accountsList.get(0).getEventsSet().iterator();
        while (itEvents.hasNext()) {
            Events e = itEvents.next();
            assertEquals(eventsList.get(0).getName(), e.getName());
        }

        itEvents = placesList.get(0).getEventsSet().iterator();
        while (itEvents.hasNext()) {
            Events e = itEvents.next();
            assertEquals(eventsList.get(0).getName(), e.getName());
        }

    }

}