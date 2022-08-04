package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.events.dto.EventsDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@Slf4j
class AccountsServiceImplTest {

    @Autowired
    AccountsService accountsService;

    @Test
    @Transactional
    void getAccountParticipateEventsTest() {
        Accounts accounts = Accounts.builder().email("cd3818@naver.com").build();
        Pageable pageable = PageRequest.of(0,9);
        List<EventsDto> accountLikesEventList = accountsService.getAccountParticipateEvents(pageable, accounts);
        log.info(accountLikesEventList.toString());
    }

    @Test
    @Transactional
    void getAccountLikesEventSizeTest() {
        Accounts accounts = Accounts.builder().email("cd3818@naver.com").build();
        Integer integer = accountsService.getAccountLikesEventSize(accounts);
        log.info("" + integer);
    }
}