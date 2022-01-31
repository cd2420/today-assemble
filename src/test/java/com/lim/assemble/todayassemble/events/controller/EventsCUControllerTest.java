package com.lim.assemble.todayassemble.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lim.assemble.todayassemble.accounts.config.WithAccount;
import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.email.service.EmailService;
import com.lim.assemble.todayassemble.events.dto.CreateEventsReq;
import com.lim.assemble.todayassemble.events.service.EventsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.path;

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

    @Autowired
    AccountsRepository accountsRepository;

    @Test
    @DisplayName("[POST] make events")
    @WithAccount("임대근")
    @Transactional
    void givenCreateEventsReq_whenCreateEventsApi_thenReturnEventsDto() throws Exception {
        // given
        String jwtToken = getJwtToken();
        CreateEventsReq createEventsReq = CreateEventsReq.builder()
                                            .name("영화모임")
                                            .description("스파이더맨 영화")
                                            .maxMembers(10)
                                            .eventsType(EventsType.OFFLINE)
                                            .eventsTime(LocalDateTime.now())
                                            .takeTime(2L)
                                            .address("서울특별시 강남구 강남대로 438 스타플렉스")
                                            .longitude("37.501646450019")
                                            .latitude("127.0262170654")
                .build();
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

}