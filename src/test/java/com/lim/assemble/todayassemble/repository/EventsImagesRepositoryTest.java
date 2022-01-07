package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.Events;
import com.lim.assemble.todayassemble.domain.entity.EventsImages;
import com.lim.assemble.todayassemble.domain.entity.Places;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import com.lim.assemble.todayassemble.type.ImagesType;
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
class EventsImagesRepositoryTest {

    @Autowired
    private EventsImagesRepository eventsImagesRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("기본 이벤트 이미지 > 기본 이벤트 이미지 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        EventsImages eventsImages = (EventsImages) EntityFactory.createEntity(Entity_Type.EVENTS_IMAGES);
        Events events = eventsImages.getEvents();
        Accounts accounts = events.getAccounts();
        Places places = events.getPlaces();

        List<EventsImages> givenEventsImagesList = new ArrayList<>();
        givenEventsImagesList.add(eventsImages);
        givenEventsImagesList.add(
                EventsImages.builder()
                        .imagesType(ImagesType.SUB)
                        .image("SUB IMAGE ~~~~~~")
                        .events(events)
                        .build()
        );

        // when
        accountsRepository.save(accounts);
        placesRepository.save(places);
        eventsRepository.save(events);
        eventsImagesRepository.saveAll(givenEventsImagesList);

        entityManager.clear();

        // then
        List<Events> eventsList = eventsRepository.findAll();
        List<EventsImages> eventsImagesList = eventsImagesRepository.findAll();

        assertEquals(eventsImagesList.size(), 2);

        Iterator<EventsImages> itEvents = eventsList.get(0).getEventsImagesSet().iterator();
        while(itEvents.hasNext()) {
            EventsImages e = itEvents.next();
            log.info(e.getImage());
        }
    }

}