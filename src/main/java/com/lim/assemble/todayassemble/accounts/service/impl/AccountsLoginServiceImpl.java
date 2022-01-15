package com.lim.assemble.todayassemble.accounts.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AccountsLoginServiceImpl implements UserDetailsService {

    private final AccountsRepository accountsRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Accounts accounts = accountsRepository.findByEmail(email)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));
        return new UserAccount(accounts);
    }

}
