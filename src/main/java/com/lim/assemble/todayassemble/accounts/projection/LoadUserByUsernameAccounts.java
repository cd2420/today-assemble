package com.lim.assemble.todayassemble.accounts.projection;

import com.lim.assemble.todayassemble.common.type.AccountsType;

public interface LoadUserByUsernameAccounts {

    String getEmail();

    String getPassword();

    AccountsType getAccountType();

    Boolean getEmailVerified();
}
