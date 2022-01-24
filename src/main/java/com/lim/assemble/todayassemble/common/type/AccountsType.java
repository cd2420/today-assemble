package com.lim.assemble.todayassemble.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountsType {

    CLIENT("ROLE_CLIENT")
    , ADMIN("ROLE_ADMIN")
    ;

    private final String type;

}
