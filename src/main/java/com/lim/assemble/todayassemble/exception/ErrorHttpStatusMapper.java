package com.lim.assemble.todayassemble.exception;

import org.springframework.http.HttpStatus;

public class ErrorHttpStatusMapper {


    public static HttpStatus mapToStatus(ErrorCode errorCode) {

        switch (errorCode) {
            case BAD_REQUEST:
            case NO_ACCOUNT:
            case NOT_EQUAL_ACCOUNT:
            case NO_REQUEST_BODY:
            case ALREADY_EXISTS_USER:
            case WRONG_EMAIL_TOKEN:
            case NOT_MATCH_PASSWORD:
            case NOT_GET_EMAIL_VERIFIED:
            case DATE_OVERLAP:
            case NO_EVENTS_ID:
            case BAD_REQUEST_ZOOMS:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}
