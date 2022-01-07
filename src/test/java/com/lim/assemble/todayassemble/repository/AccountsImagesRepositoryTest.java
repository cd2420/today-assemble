package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import com.lim.assemble.todayassemble.domain.entity.AccountsImages;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountsImagesRepositoryTest {

    @Autowired
    private AccountsImagesRepository accountsImagesRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Test
    @DisplayName("기본 계정 > 기본 계정 이미지 생성")
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        AccountsImages basicAccountsImage = (AccountsImages) EntityFactory.createEntity(Entity_Type.ACCOUNTS_IMAGES);
        Accounts basicAccounts = basicAccountsImage.getAccounts();

        // when
        accountsRepository.save(basicAccounts);
        accountsImagesRepository.save(basicAccountsImage);


        // then
        List<Accounts> accountsList = accountsRepository.findAll();
        List<AccountsImages> accountsImagesList = accountsImagesRepository.findAll();

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