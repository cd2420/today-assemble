package com.lim.assemble.todayassemble.accounts.controller;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.accounts.service.impl.AccountsLoginServiceImpl;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountsController.class)
class AccountsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountsService accountsService;

    @MockBean
    AccountsLoginServiceImpl accountsLoginService;

    @Test
    @DisplayName("[GET] account List")
    void givenTwoAccountsDtoNoEventNoEmailNoLikesNoImage_whenGetAccountListAPI_thenCheckAccountsDto()
        throws Exception
    {
        // given
        List<AccountsDto> accountsDtoList = new ArrayList<>();
        for(int i =0; i < 3; i ++) {
            accountsDtoList.add(
                    AccountsDto.builder()
                            .name("abc" + i)
                            .email("abc" + i + "@gamil.com")
                            .accountType(AccountsType.CLIENT)
                            .gender(Gender.MALE)
                            .emailVerified(true)
                            .emailCheckToken("asdfasdfzxcv")
                            .build()
            );
        }
        given(accountsService.getAccountList())
                .willReturn(accountsDtoList);

        // when && then
        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andDo(print())
        ;

    }



}