package com.lim.assemble.todayassemble.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lim.assemble.todayassemble.accounts.config.WithAccount;
import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class EventsCUControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventsService eventsService;

    @MockBean
    EmailService emailService;

    @Test
    @DisplayName("[POST] make events")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReq_whenCreateEventsApi_thenReturnEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        String json = asJsonString(createEventsReq);

        // when
        // then
        MvcResult result = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        log.info("$$$$$result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] make events with image and tags")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReqWithImagesAndTags_whenCreateEventsApi_thenReturnEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        EventsImagesDto eventsImagesDto = new EventsImagesDto();
        eventsImagesDto.setImagesType(ImagesType.MAIN);
        eventsImagesDto.setImage("main image");
        Set<EventsImagesDto> eventsImagesDtos = new HashSet<>();
        eventsImagesDtos.add(eventsImagesDto);

        TagsDto tagsDto = new TagsDto();
        tagsDto.setName("영화");
        Set<TagsDto> tagsDtos = new HashSet<>();
        tagsDtos.add(tagsDto);


        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , eventsImagesDtos
                , tagsDtos
        );
        String json = asJsonString(createEventsReq);

        // when
        // then
        MvcResult result = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();


        log.info("$$$$$result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] make events with zooms")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReqWithZooms_whenCreateEventsApi_thenReturnEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();


        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.ONLINE
                , null
                , null
        );
        String json = asJsonString(createEventsReq);

        // when
        // then
        MvcResult result = mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();


        log.info("$$$$$result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] EXCEPTION - make events > overlap time")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReqOverlapTime_whenCreateEventsApi_thenReturnException() throws Exception {
        // given
        String jwtToken = getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        eventsService.createEvents(createEventsReq, userAccount.getAccounts());
        String json = asJsonString(createEventsReq);

        // when
        // then
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result -> assertEquals(
                                ErrorCode.DATE_OVERLAP.getMessage()
                                , result.getResolvedException().getMessage()
                        )
                );
    }

    @Test
    @DisplayName("[POST] EXCEPTION - make events > EventsType: OFFLINE > Not exist necessary value")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReqNotHaveAddressValue_whenCreateEventsApi_thenReturnException() throws Exception {
        // given
        String jwtToken = getJwtToken();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        createEventsReq.setAddress("");
        createEventsReq.setLongitude("");
        createEventsReq.setAddress("");
        String json = asJsonString(createEventsReq);

        // when
        // then
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result -> assertEquals(
                                "OFFLINE > 주소 값 누락"
                                , result.getResolvedException().getMessage()
                        )
                );
    }

    @Test
    @DisplayName("[POST] EXCEPTION - make events > EventsType: ONLINE > Not exist necessary value")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReqNotHaveZoomValue_whenCreateEventsApi_thenReturnException() throws Exception {
        // given
        String jwtToken = getJwtToken();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.ONLINE
                , null
                , null
        );
        createEventsReq.setZoomsSet(new HashSet<>());
        String json = asJsonString(createEventsReq);

        // when
        // then
        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(
                        result -> assertEquals(
                                "ONLINE > ZOOM 값 누락"
                                , result.getResolvedException().getMessage()
                        )
                );
    }

    @Test
    @DisplayName("[PUT] update events")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsContentsReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());

        UpdateEventsContentsReq updateEventsContentsReq = new UpdateEventsContentsReq();
        updateEventsContentsReq.setId(eventsDto.getId());
        updateEventsContentsReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsContentsReq.setName("~~~~수정~~~");
        updateEventsContentsReq.setDescription("!!!!!!!수정 확인!!!!");
        updateEventsContentsReq.setMaxMembers(15);
        updateEventsContentsReq.setEventsTime(LocalDateTime.now());
        updateEventsContentsReq.setTakeTime(2L);

        String json = asJsonString(updateEventsContentsReq);

        // when
        // then
        MvcResult result = mockMvc.perform(put("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();
        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[PUT] update eventsImages")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsImagesReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );

        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());

        EventsImagesDto eventsImagesDto1 = new EventsImagesDto();
        eventsImagesDto1.setImagesType(ImagesType.MAIN);
        eventsImagesDto1.setImage("메인 이미지 수정");
        EventsImagesDto eventsImagesDto2 = new EventsImagesDto();
        eventsImagesDto2.setImagesType(ImagesType.SUB);
        eventsImagesDto2.setImage("서브 이미지 수정");

        Set<EventsImagesDto> images = new HashSet<>();
        images.add(eventsImagesDto1);
        images.add(eventsImagesDto2);

        UpdateEventsImagesReq updateEventsImagesReq = new UpdateEventsImagesReq();
        updateEventsImagesReq.setId(eventsDto.getId());
        updateEventsImagesReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsImagesReq.setImages(images);

        String json = asJsonString(updateEventsImagesReq);

        // when
        // then
        MvcResult result = mockMvc.perform(put("/api/v1/events/images")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();
        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[PUT] update eventsTags")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsTagsReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );

        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());

        Set<String> tags = new HashSet<String>(Arrays.asList("태그 수정 1", " 태그 수정 2"));

        UpdateEventsTagsReq updateEventsTagsReq = new UpdateEventsTagsReq();
        updateEventsTagsReq.setId(eventsDto.getId());
        updateEventsTagsReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsTagsReq.setTags(tags);

        String json = asJsonString(updateEventsTagsReq);

        // when
        // then
        MvcResult result = mockMvc.perform(put("/api/v1/events/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();
        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[PUT] update eventsType - ONLINE")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsTypeOnlineReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.ONLINE
                , null
                , null
        );

        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());
        Set<ZoomsDto> zoomsDtos = new HashSet<>();
        zoomsDtos.add(ZoomsDto.builder().url("수정 url~~~~").status(true).build());

        UpdateEventsTypeReq updateEventsTypeReq = UpdateEventsTypeReq.builder().build();
        updateEventsTypeReq.setId(eventsDto.getId());
        updateEventsTypeReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsTypeReq.setEventsType(EventsType.ONLINE);
        updateEventsTypeReq.setZooms(zoomsDtos);

        String json = asJsonString(updateEventsTypeReq);

        // when
        // then
        MvcResult result = mockMvc.perform(put("/api/v1/events/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();
        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("[PUT] update eventsType - OFFLINE")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsTypeOfflineReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );

        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());
        Set<ZoomsDto> zoomsDtos = new HashSet<>();
        zoomsDtos.add(ZoomsDto.builder().url("수정 url~~~~").status(true).build());

        UpdateEventsTypeReq updateEventsTypeReq = UpdateEventsTypeReq.builder().build();
        updateEventsTypeReq.setId(eventsDto.getId());
        updateEventsTypeReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsTypeReq.setEventsType(EventsType.OFFLINE);
        updateEventsTypeReq.setAddress("수정 주소");
        updateEventsTypeReq.setLatitude("수정 위도");
        updateEventsTypeReq.setLongitude("수정 경도");

        String json = asJsonString(updateEventsTypeReq);

        // when
        // then
        MvcResult result = mockMvc.perform(put("/api/v1/events/type")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();
        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("[PUT] EXCEPTION: update events > not equals host accounts")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsContentsReqNotEqualsHostAccounts_whenWrongHostAccounts_thenReturnException() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        EventsDto eventsDto = eventsService.getEventsList(pageable).get(0);

        UpdateEventsContentsReq updateEventsContentsReq = new UpdateEventsContentsReq();
        updateEventsContentsReq.setId(eventsDto.getId());
        updateEventsContentsReq.setAccountsId(userAccount.getAccounts().getId());
        updateEventsContentsReq.setName("~~~~수정~~~");
        updateEventsContentsReq.setDescription("!!!!!!!수정 확인!!!!");
        updateEventsContentsReq.setMaxMembers(15);
        updateEventsContentsReq.setEventsTime(LocalDateTime.now());
        updateEventsContentsReq.setTakeTime(2L);

        String json = asJsonString(updateEventsContentsReq);

        // when
        // then
        mockMvc.perform(put("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(ErrorCode.NOT_EQUAL_ACCOUNT.getMessage(), result.getResolvedException().getMessage()))
        ;

    }

    @Test
    @DisplayName("[DELETE] delete events")
    @WithAccount("임대근")
    @Transactional
    void givenDeleteEventsId_whenDeleteEventsAPI_thenReturnStatusOk() throws Exception {
        // given
        String jwtToken = getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto eventsDto = eventsService.createEvents(createEventsReq, userAccount.getAccounts());
        // when
        // then
        mockMvc.perform(delete("/api/v1/events/" + eventsDto.getId())
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
        ;

    }

    @Test
    @DisplayName("[DELETE] EXCEPTION: delete events > not equals host accounts")
    @WithAccount("임대근")
    @Transactional
    void givenDeleteEventsIdNotEqualsHostAccounts_whenDeleteEventsAPI_thenReturnStatusOk() throws Exception {
        // given
        String jwtToken = getJwtToken();

        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        EventsDto eventsDto = eventsService.getEventsList(pageable).get(0);
        // when
        // then
        mockMvc.perform(delete("/api/v1/events/" + eventsDto.getId())
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(
                        ErrorCode.NOT_EQUAL_ACCOUNT.getMessage()
                        , result.getResolvedException().getMessage()
                        )
                )
        ;

    }

    private String getJwtToken() {
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final long EXPIRATION_TIME = 864_000_00;
        final String SIGNING_KEY = "signingKey";

        return Jwts.builder().setSubject(userAccount.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CreateEventsReq getCreateEventsReq(
            EventsType eventsType
            , Set<EventsImagesDto> eventsImagesDtos
            , Set<TagsDto> tagsDtos
    ) {
        CreateEventsReq createEventsReq = null;
        if (EventsType.OFFLINE.equals(eventsType)) {
            createEventsReq = CreateEventsReq.builder()
                    .name("영화모임")
                    .description("스파이더맨 영화")
                    .maxMembers(10)
                    .eventsType(eventsType)
                    .eventsTime(LocalDateTime.now())
                    .takeTime(2L)
                    .eventsImagesSet(eventsImagesDtos)
                    .tagsSet(tagsDtos)
                    .address("서울특별시 강남구 강남대로 438 스타플렉스")
                    .longitude("37.501646450019")
                    .latitude("127.0262170654")
                    .build();
        } else {

            ZoomsDto zoomsDto = ZoomsDto.builder()
                    .status(true)
                    .url("www.~~~~~~")
                    .build();
            Set<ZoomsDto> zoomsDtos = new HashSet<>();
            zoomsDtos.add(zoomsDto);

            createEventsReq = CreateEventsReq.builder()
                    .name("영화모임")
                    .description("스파이더맨 영화")
                    .maxMembers(10)
                    .eventsType(eventsType)
                    .eventsTime(LocalDateTime.now())
                    .takeTime(2L)
                    .eventsImagesSet(eventsImagesDtos)
                    .tagsSet(tagsDtos)
                    .zoomsSet(zoomsDtos)
                    .build();
        }

        return createEventsReq;
    }

}