package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.common.type.EventsType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
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
    EntityManager entityManager;

    @Test
    @DisplayName("기본 이벤트 저장 > 계정과 장소에 맵핑되는지 확인")
    @Transactional
    void givenBasicData_whenSaveRepository_thenCheckSettingAccountsEventsAndPlacesEvents() {
        //given
        Events events = (Events) EntityFactory.createEntity(Entity_Type.EVENTS);
        Accounts accounts = events.getAccounts();

        accountsRepository.save(accounts);
        eventsRepository.save(events);

        entityManager.clear();

        // when
        List<Events> eventsList = eventsRepository.findAll();
        List<Accounts> accountsList = accountsRepository.findAll();

        // then
        Iterator<Events> itEvents = accountsList.get(0).getEventsSet().iterator();
        while (itEvents.hasNext()) {
            Events e = itEvents.next();
            assertEquals(eventsList.get(0).getName(), e.getName());
        }

    }

    @Test
    @DisplayName("기본 이벤트 저장 > 한 계정, 한 장소에 여러 이벤트 맵핑되는지 확인")
    @Transactional
    void givenSeveralEventsAndOneAccounts_whenSaveRepository_thenCheckSettingAccountsEventsAndEvents() {
        //given
        Accounts accounts = (Accounts) EntityFactory.createEntity(Entity_Type.ACCOUNTS);

        List<Events> givenEventsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            givenEventsList.add(
                    Events.builder()
                    .name("스파이더맨 보러갈 모임" + i)
                    .host_email(accounts.getEmail())
                    .description("영화 모임~~~~~~~~~~")
                    .accounts(accounts)
                    .maxMembers(4)
                    .likes(0)
                    .eventsType(EventsType.OFFLINE)
                    .build()
            );
        }

        // when
        accountsRepository.save(accounts);
        eventsRepository.saveAll(givenEventsList);

        entityManager.clear();

        // then
        List<Events> eventsList = eventsRepository.findAll();
        List<Accounts> accountsList = accountsRepository.findAll();

        assertEquals(eventsList.size(), 10);

        Iterator<Events> itEvents = accountsList.get(0).getEventsSet().iterator();
        while (itEvents.hasNext()) {
            Events e = itEvents.next();
            log.info(e.getName());
        }

        while (itEvents.hasNext()) {
            Events e = itEvents.next();
            log.info(e.getName());
        }

    }

}