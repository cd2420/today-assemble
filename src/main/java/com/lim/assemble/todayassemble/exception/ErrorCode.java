package com.lim.assemble.todayassemble.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST("잘못된 요청")
    , NO_ACCOUNT("해당 아이디가 없습니다.")
    , NO_REQUEST_BODY("request 값이 잘못되었습니다.")
    , ALREADY_EXISTS_USER("중복 되는 아이디입니다.")

    , FAIL_TO_SEND_EMAIL("이메일 전송에 실패했습니다.")
    , WRONG_EMAIL_TOKEN("이메일 토큰이 잘못되었습니다.")
    ;

    private final String message;
}
