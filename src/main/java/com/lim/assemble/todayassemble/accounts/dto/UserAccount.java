package com.lim.assemble.todayassemble.accounts.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class UserAccount extends User {

    private Accounts accounts;

    public UserAccount(Accounts accounts) {
        super(accounts.getEmail(), accounts.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        this.accounts = accounts;
    }
}
