package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.config.CreateEventsReqFactory;
import com.lim.assemble.todayassemble.accounts.config.JsonToString;
import com.lim.assemble.todayassemble.accounts.config.WithAccount;
import com.lim.assemble.todayassemble.accounts.config.WithAccountSecurityContextFacotry;
import com.lim.assemble.todayassemble.accounts.dto.*;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.common.message.ValidationMessage;
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

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("[POST] create accounts")
    @Transactional
    void givenCreateReq_whenSaveAccounts_thenCheckDB () throws Exception {
        // given
        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("check1@gmail.com");
        createAccountReq.setName("check1");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
        createAccountReq.setBirth(LocalDateTime.now());

        String json = JsonToString.asJsonString(createAccountReq);

        // when
        mockMvc.perform(post("/api/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.name").value(createAccountReq.getName()))
                .andExpect(jsonPath("$.email").value(createAccountReq.getEmail()))
                .andExpect(jsonPath("$.age").value(createAccountReq.getAge()))
                .andExpect(jsonPath("$.gender").value(createAccountReq.getGender().getGender()))
                ;
    }

    @Test
    @DisplayName("[POST] EXCEPTION - create accounts > 입력값 에러 > 이메일 ")
    @Transactional
    void givenWrongEmailCreateReq_whenSaveAccounts_thenException() throws Exception {
        // given
        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("check1!gmail.com");
        createAccountReq.setName("check1");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
        createAccountReq.setBirth(LocalDateTime.now());

        String json = JsonToString.asJsonString(createAccountReq);

        // when
        mockMvc.perform(post("/api/v1/accounts/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.msg").value(ValidationMessage.WRONG_EMAIL_FORM))
        ;
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

    @Test
    @DisplayName("[PUT] update Accounts - update name")
    @Transactional
    @WithAccount("임대근")
    void givenUpdateAccountsName_whenUpdateAccountsApi_thenAccountsDto() throws Exception {
        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UpdateAccountsReq updateAccountsReq = new UpdateAccountsReq();
        Accounts accounts = userAccount.getAccounts();

        updateAccountsReq.setName("김김대근");
        updateAccountsReq.setPassword("asdfasdf");
        String json = JsonToString.asJsonString(updateAccountsReq);

        mockMvc.perform(put("/api/v1/accounts/" + accounts.getId())
                        .header("Authorization", "Bearer" + " " + jwtToken)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.name").value(updateAccountsReq.getName()))
        ;
    }

    @Test
    @DisplayName("[PUT] update Accounts - update password")
    @Transactional
    @WithAccount("임대근")
    void givenUpdateAccountsPassword_whenUpdateAccountsApi_thenAccountsDto() throws Exception {
        String jwtToken = WithAccountSecurityContextFacotry.getJwtToken();
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UpdateAccountsReq updateAccountsReq = new UpdateAccountsReq();
        Accounts accounts = userAccount.getAccounts();

        updateAccountsReq.setName(accounts.getName());
        updateAccountsReq.setPassword("newPassword");
        String json = JsonToString.asJsonString(updateAccountsReq);

        MvcResult result = mockMvc.perform(put("/api/v1/accounts/" + accounts.getId())
                                            .header("Authorization", "Bearer" + " " + jwtToken)
                                            .with(csrf())
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(json)
                                            .accept(MediaType.APPLICATION_JSON)
                                    ).andExpect(status().isOk())
                                            .andExpect(jsonPath("$").isMap())
                                            .andReturn()
                ;
        log.info("$$$$$$$$$$$ result: {}", result.getResponse().getContentAsString());
    }

}