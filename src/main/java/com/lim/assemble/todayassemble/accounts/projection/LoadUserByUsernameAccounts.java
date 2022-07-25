package com.lim.assemble.todayassemble.accounts.projection;

import com.lim.assemble.todayassemble.common.type.AccountsType;
import com.lim.assemble.todayassemble.common.type.Gender;

import java.time.LocalDateTime;

public interface LoadUserByUsernameAccounts {

    String getEmail();

    String getPassword();

    String getName();

    AccountsType getAccountType();

    Boolean getEmailVerified();

    LocalDateTime getBirth();

    Gender getGender();
}
