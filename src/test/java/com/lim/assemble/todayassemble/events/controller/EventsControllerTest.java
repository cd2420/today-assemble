package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class EventsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventsService eventsService;

    @Test
    @DisplayName("[GET] Events List")
    @Transactional(readOnly = true)
    void givenPaging_whenGetEventsListApi_thenPrintEventsList() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(get("/api/v1/events?page=0"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
        ;
        Pageable pageable = PageRequest.of(0, 9, Sort.Direction.DESC, "createdAt");
        List<EventsDto> eventsList = eventsService.getEventsList(pageable);
        log.info("eventsDto : {}", eventsList.toString());
        log.info("eventsDtoSize : {}", eventsList.size());
    }

    @Test
    @DisplayName("[GET] Events Detail")
    @Transactional(readOnly = true)
    void givenEventsId_whenGetEventsDetailApi_thenPrintEvents() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        Long eventsId = eventsService.getEventsList(pageable).get(0).getId();
        // when
        // then
        mockMvc.perform(get("/api/v1/events/" + eventsId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
        ;
        EventsDto events = eventsService.getEvents(3L);
        log.info("eventsDto : {}", events.toString());
    }

    @Test
    @DisplayName("[GET] No Events Exception")
    @Transactional(readOnly = true)
    void givenNoEventsId_whenGetEventsDetailApi_thenException() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        Long eventsId = eventsService.getEventsList(pageable) == null ? 1L: eventsService.getEventsList(pageable).get(0).getId() + 1L;

        // when
        // then
        mockMvc.perform(get("/api/v1/events/" + eventsId))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(ErrorCode.NO_EVENTS_ID.getMessage(), result.getResolvedException().getMessage()))
        ;
    }

}