package com.lim.assemble.todayassemble.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.accounts.service.impl.AccountsLoginServiceImpl;
import com.lim.assemble.todayassemble.common.type.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsCUController.class)
class AccountsCUControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountsService accountsService;

    @MockBean
    AccountsLoginServiceImpl accountsLoginService;


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


    /**
     * todo: 실제 생성된 데이터 가지고 테스트 코드 작성
     */

}