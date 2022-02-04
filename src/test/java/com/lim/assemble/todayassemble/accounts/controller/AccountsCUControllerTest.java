package com.lim.assemble.todayassemble.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim.assemble.todayassemble.accounts.config.CreateEventsReqFactory;
import com.lim.assemble.todayassemble.accounts.config.WithAccount;
import com.lim.assemble.todayassemble.accounts.config.WithAccountSecurityContextFacotry;
import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AccountsCUControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountsService accountsService;

    @MockBean
    EmailService emailService;

    @Autowired
    EventsService eventsService;

    /**
     * 임의 데이터 생성으로 테스트
     */
    @Test
    @DisplayName("[POST] create accounts - 입력값 정상")
    void givenCreateReq_whenSaveAccounts_thenCheckDB () throws Exception {
        // given
        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("check1@gmail.com");
        createAccountReq.setName("check1");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
//        createAccountReq.setBirth(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createAccountReq);

        // when
        mockMvc.perform(post("/api/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isOk())
                ;
    }

    @Test
    @DisplayName("[POST] create accounts - 입력값 에러 > 이메일 ")
    void givenWrongEmailCreateReq_whenSaveAccounts_thenException () throws Exception {
        // given
        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("check1!gmail.com");
        createAccountReq.setName("check1");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
//        createAccountReq.setBirth(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createAccountReq);

        // when
        mockMvc.perform(post("/api/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    @DisplayName("[POST] login accounts - 정상적 로그인 ")
    @Transactional
    void givenDBAccounts_whenLogin_thenGetJWT() throws Exception {
        // given
        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("check1@gmail.com");
        createAccountReq.setName("check1");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
        createAccountReq.setBirth(LocalDateTime.now());


        CreateAccountReq createAccountReq2 = new CreateAccountReq();
        createAccountReq.setEmail("check1@gmail.com");
        createAccountReq.setPassword("asdfasdf");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(createAccountReq2);

        AccountsDto accountsDto = accountsService.signUp(createAccountReq);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));
//                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("[POST] manageAccountLikesEvent - create Likes")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenManageAccountsCreateLikeEventApi_thenOK() throws Exception {
        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto events = eventsService.createEvents(createEventsReq, userAccount.getAccounts());

        MvcResult result = mockMvc.perform(post("/api/v1/accounts/likes/events/" + events.getId())
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn();

        log.info("$$$$$$$$ result: {}", result.getResponse().getContentAsString());

    }

    @Test
    @DisplayName("[POST] manageAccountLikesEvent - delete Likes")
    @WithAccount("임대근")
    @Transactional
    void givenEventsId_whenManageAccountsDeleteLikeEventApi_thenOK() throws Exception {
        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CreateEventsReq createEventsReq = CreateEventsReqFactory.getCreateEventsReq(
                EventsType.OFFLINE
                , null
                , null
        );
        EventsDto events = eventsService.createEvents(createEventsReq, userAccount.getAccounts());
        events = accountsService.manageAccountLikesEvent(events.getId(), userAccount.getAccounts());
        log.info("$$$$$$$$ original events_likes: {}", events.getLikes());

        MvcResult result = mockMvc.perform(post("/api/v1/accounts/likes/events/" + events.getId())
                .header("Authorization", "Bearer" + " " + jwtToken)
                .with(csrf())
        )
                .andExpect(status().isOk())
                .andReturn();

        log.info("$$$$$$$$ result: {}", result.getResponse().getContentAsString());

    }

}