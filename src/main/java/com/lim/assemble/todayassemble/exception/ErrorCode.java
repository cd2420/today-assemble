package com.lim.assemble.todayassemble.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    /////////////// client쪽 문제
    BAD_REQUEST("잘못된 요청")
    , NO_ACCOUNT("해당 아이디가 없습니다.")
    , NOT_EQUAL_ACCOUNT("아이디가 일치하지 않습니다.")
    , NO_REQUEST_BODY("request 값이 잘못되었습니다.")
    , ALREADY_EXISTS_USER("중복 되는 아이디입니다.")
    , WRONG_EMAIL_TOKEN("이메일 토큰이 잘못되었습니다.")
    , NOT_MATCH_PASSWORD("패스워드가 일치하지 않습니다.")
    , NOT_GET_EMAIL_VERIFIED("이메일 인증을 수락하지 않았습니다.")
    , WRONG_JWT("이메일 인증을 수락하지 않았습니다.")

    , DATE_OVERLAP("날짜가 겹칩니다")
    , NO_EVENTS_ID("잘못된 event id입니다.")
    , NO_LIKES_ID("잘못된 \"좋아요\" id입니다.")
    , NO_ACCOUNTS_MAPPER_EVENTS_ID("잘못된 \"모임 참여\" id입니다.")
    , OVER_MAX_MEMBER("인원 초과")
    , ALREADY_INVITE_ACCOUNTS("이미 초대된 계정입니다.")
    , NO_ACCOUNTS_IN_EVENTS("초대 되지 않은 계정입니다.")
    , OVER_MAIN_IMAGES("MAIN 이미지는 하나만 가능합니다.")
    , IMPOSSIBLE_PARTICIPATE_TIME("참여 가능한 시간대가 아닙니다.")

    //////////////// server쪽 문제
    , FAIL_TO_SEND_EMAIL("이메일 전송에 실패했습니다.")
    , FAILED_AUTHENTICATION("인증에 실패했습니다.")
    ;

    private final String message;
}
