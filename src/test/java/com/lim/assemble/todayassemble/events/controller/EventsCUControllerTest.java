package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.accounts.config.CreateEventsReqFactory;
import com.lim.assemble.todayassemble.accounts.config.JsonToString;
import com.lim.assemble.todayassemble.accounts.config.WithAccount;
import com.lim.assemble.todayassemble.accounts.config.WithAccountSecurityContextFactory;
import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.tags.dto.TagsDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class EventsCUControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EventsService eventsService;

    @Autowired
    AccountsService accountsService;

    @Autowired
    WebApplicationContext context;

    @MockBean
    EmailService emailService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("[POST] make events")
    @WithAccount("TEST")
    @Transactional
    void givenCreateEventsReq_whenCreateEventsApi_thenReturnEventsDto() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

        EventsImagesDto eventsImagesDto = new EventsImagesDto();
        eventsImagesDto.setImagesType(ImagesType.MAIN);
        eventsImagesDto.setImage("main image");
        Set<EventsImagesDto> eventsImagesDtos = new HashSet<>();
        eventsImagesDtos.add(eventsImagesDto);

        TagsDto tagsDto = new TagsDto();
        tagsDto.setName("영화");
        Set<TagsDto> tagsDtos = new HashSet<>();
        tagsDtos.add(tagsDto);


        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();


        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        eventsService.createEvents(createEventsReq, (Accounts) userAccount.getAccounts());
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
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
    @DisplayName("[PUT] update events")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsContentsReq_whenUpdateEventsApi_thenEventsDto() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto eventsDto = eventsService.createEvents(createEventsReq, (Accounts) userAccount.getAccounts());

        UpdateEventsContentsReq updateEventsContentsReq = new UpdateEventsContentsReq();
        updateEventsContentsReq.setId(eventsDto.getId());
        updateEventsContentsReq.setAccountsId(((Accounts) userAccount.getAccounts()).getId());
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );

        EventsDto eventsDto = eventsService.createEvents(createEventsReq, (Accounts) userAccount.getAccounts());

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
        updateEventsImagesReq.setAccountsId(((Accounts) userAccount.getAccounts()).getId());
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
    @DisplayName("[PUT] EXCEPTION: update events > not equals host accounts")
    @WithAccount("임대근")
    @Transactional
    void givenUpdateEventsContentsReqNotEqualsHostAccounts_whenWrongHostAccounts_thenReturnException() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        EventsDto eventsDto = eventsService.getEventsList(pageable).get(0);

        UpdateEventsContentsReq updateEventsContentsReq = new UpdateEventsContentsReq();
        updateEventsContentsReq.setId(eventsDto.getId());
        updateEventsContentsReq.setAccountsId(((Accounts) userAccount.getAccounts()).getId());
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
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto eventsDto = eventsService.createEvents(createEventsReq, (Accounts) userAccount.getAccounts());
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
    void givenDeleteEventsIdNotEqualsHostAccounts_whenDeleteEventsAPI_thenReturnErrorMSG() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();

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

    @Test
    @DisplayName("[POST] events participate")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenParticipateEventsAPI_thenReturnStatusOk() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        EventsDto eventsDto = eventsService.getEventsList(pageable).get(0);
        // when
        // then
        MvcResult result = mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts")
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn();

        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] events leave")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenLeaveEventsAPI_thenReturnStatusOk() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt");
        EventsDto eventsDto = eventsService.getEventsList(pageable).get(0);
        // when
        // then
        mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts")
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        ).andExpect(status().isOk())
        ;

        MvcResult result = mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts")
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn();


        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] invite events")
    @WithAccount("임대근")
    @Transactional
    void givenEventsIdAndAccountsId_whenInviteEventsAPI_thenReturnStatusOk() throws Exception {
        // given
        String jwtToken = WithAccountSecurityContextFactory.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto eventsDto = eventsService.createEvents(createEventsReq, (Accounts) userAccount.getAccounts());
        AccountsDto account = accountsService.getAccount(1l);
        // when
        // then
        MvcResult result = mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts/" + account.getId())
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn();


        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[PUT] response events > Accept")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenResponseEventsInviteAcceptAPI_thenReturnStatusOk() throws Exception {
        // given
//        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
//        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Accounts targetAccounts = userAccount.getAccounts();
//        AccountsDto hostAccounts = accountsService.getAccount(1l);
//        EventsDto eventsDto = hostAccounts.getEventsDtos().stream()
//                .filter(item -> item.getHostAccountsId().equals(hostAccounts.getId()))
//                .findFirst()
//                .get();
//
//        mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts/" + targetAccounts.getId())
//                .header("Authorization", "Bearer" + " " + WithAccountSecurityContextFacotry.getJwtToken(hostAccounts.getEmail()) )
//                .with(csrf())
//                )
//                .andExpect(status().isOk())
//        ;
//        UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq = UpdateAccountsMapperEventsReq.builder().response(true).build();
//        String json = asJsonString(updateAccountsMapperEventsReq);
//        // when
//        // then
//        MvcResult result = mockMvc.perform(put("/api/v1/events/" + eventsDto.getId() + "/accounts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .header("Authorization", "Bearer" + " " + jwtToken)
//                .with(csrf())
//        )
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[PUT] response events > Reject")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenResponseEventsInviteRejectAPI_thenReturnStatusOk() throws Exception {
        // given
//        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
//        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Accounts targetAccounts = userAccount.getAccounts();
//        AccountsDto hostAccounts = accountsService.getAccount(1l);
//        EventsDto eventsDto = hostAccounts.getEventsDtos().stream()
//                .filter(item -> item.getHostAccountsId().equals(hostAccounts.getId()))
//                .findFirst()
//                .get();
//
//        mockMvc.perform(post("/api/v1/events/" + eventsDto.getId() + "/accounts/" + targetAccounts.getId())
//                .header("Authorization", "Bearer" + " " + WithAccountSecurityContextFacotry.getJwtToken(hostAccounts.getEmail()) )
//                .with(csrf())
//        )
//                .andExpect(status().isOk())
//        ;
//        UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq = UpdateAccountsMapperEventsReq.builder().response(false).build();
//        String json = asJsonString(updateAccountsMapperEventsReq);
//        // when
//        // then
//        MvcResult result = mockMvc.perform(put("/api/v1/events/" + eventsDto.getId() + "/accounts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .header("Authorization", "Bearer" + " " + jwtToken)
//                .with(csrf())
//        )
//                .andExpect(status().isOk())
//                .andReturn();
//
//
//        log.info("$$$$$$$$$ result : {}", result.getResponse().getContentAsString());

    }


    public String asJsonString(final Object obj) {
        return JsonToString.asJsonString(obj);
    }

}