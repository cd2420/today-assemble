package com.lim.assemble.todayassemble.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountsType {

    CLIENT("CLIENT")
    , ADMIN("ADMIN")
    ;

    private final String type;

}
