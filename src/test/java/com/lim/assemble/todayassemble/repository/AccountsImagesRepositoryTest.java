package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.AccountsImages;
import com.lim.assemble.todayassemble.type.AccountsType;
import com.lim.assemble.todayassemble.type.Gender;
import com.lim.assemble.todayassemble.type.ImagesType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountsImagesRepositoryTest {

    @Autowired
    private AccountsImagesRepository accountsImagesRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    Accounts basicAccounts = Accounts.builder()
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

    AccountsImages basicAccountsImage = AccountsImages.builder()
            .accounts(basicAccounts)
            .image("path~~~~~~~~")
            .imagesType(ImagesType.MAIN)
            .build();

    @Test
    @DisplayName("기본 계정 > 기본 계정 이미지 생성")
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        accountsRepository.save(basicAccounts);
        accountsImagesRepository.save(basicAccountsImage);

        // when
        List<Accounts> accountsList = accountsRepository.findAll();
        List<AccountsImages> accountsImagesList = accountsImagesRepository.findAll();

        // then
        assertTrue(accountsList.size() == 1);
        assertTrue(accountsImagesList.size() == 1);

        assertEquals(
                accountsImagesList.get(0).getAccounts().getId()
                , accountsList.get(0).getId()
        );

        assertEquals(
                accountsList.get(0).getAccountsImages().getId()
                , accountsImagesList.get(0).getId()
        );
    }


}