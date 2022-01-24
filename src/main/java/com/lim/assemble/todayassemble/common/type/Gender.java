package com.lim.assemble.todayassemble.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE")
    ;

    private final String gender;
}
