package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.type.AccountsType;
import com.lim.assemble.todayassemble.type.Gender;
import org.junit.After;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountsRepositoryTest {

    @Autowired
    private AccountsRepository accountsRepository;

    Accounts basic = Accounts.builder()
            .name("limdaegeun")
            .email("asdw@asdw.com")
            .password("adwdzcxasd")
            .accountType(AccountsType.CLIENT)
            .gender(Gender.MALE)
            .birth(LocalDateTime.now())
            .age(30)
            .emailVerified(true)
            .emailCheckToken("asdwdadwasdw")
            .build();

    @Test
    @DisplayName("기본 - 저장")
    public void givenBasicData_whenSaveRepository_thenReturnsBasicData(){
        //given
        accountsRepository.save(basic);

        //when
        List<Accounts> accountsList = accountsRepository.findAll();

        //then
        assertTrue(accountsList.size() == 1);
        assertNotNull(accountsList.get(0).getId());
        assertNotNull(accountsList.get(0).getCreatedAt());
        assertNotNull(accountsList.get(0).getUpdatedAt());

    }

}