package com.lim.assemble.todayassemble.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST("서버 문제");

    private final String message;
}
