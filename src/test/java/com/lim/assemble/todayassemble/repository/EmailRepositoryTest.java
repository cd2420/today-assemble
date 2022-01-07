package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.email.entity.Email;
import com.lim.assemble.todayassemble.email.repository.EmailRepository;
import com.lim.assemble.todayassemble.entity.EntityFactory;
import com.lim.assemble.todayassemble.entity.Entity_Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailRepositoryTest {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("기본 이메일 > 기본 이메일 생성")
    @Transactional
    void givenBasicData_whenSaveRepository_thenReturnsBasicData() {
        // given
        Email email = (Email) EntityFactory.createEntity(Entity_Type.EMAIL);
        Accounts accounts = email.getAccounts();

        // when
        accountsRepository.save(accounts);
        emailRepository.save(email);
        entityManager.clear();

        // then
        List<Accounts> accountsList = accountsRepository.findAll();
        List<Email> emailList = emailRepository.findAll();

        Iterator<Email> it = accountsList.get(0).getEmailSet().iterator();
        while(it.hasNext()) {
            Email e = it.next();
            assertEquals(emailList.get(0).getId(), e.getId());
        }

    }


}