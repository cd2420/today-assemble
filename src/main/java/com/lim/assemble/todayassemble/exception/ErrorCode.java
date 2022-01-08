package com.lim.assemble.todayassemble.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST("잘못된 요청"),
    NO_ACCOUNT("해당 아이디가 없습니다.");

    private final String message;
}
