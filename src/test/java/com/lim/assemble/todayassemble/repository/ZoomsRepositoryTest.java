package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import com.lim.assemble.todayassemble.zooms.repository.ZoomsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ZoomsRepositoryTest {

    @Autowired
    private ZoomsRepository zoomsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("기본 줌 > 기본 줌 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        Zooms zooms = (Zooms) EntityFactory.createEntity(Entity_Type.ZOOMS);
        Events events = zooms.getEvents();
        Accounts accounts = events.getAccounts();

        // when
        accountsRepository.save(accounts);
        eventsRepository.save(events);
        zoomsRepository.save(zooms);

        entityManager.clear();

        // then
        List<Zooms> zoomsList = zoomsRepository.findAll();

        Zooms check = zoomsList.get(0);
        assertNotNull(check);
        assertEquals(check.getEvents().getName(), events.getName());
    }

}