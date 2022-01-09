package com.lim.assemble.todayassemble.exception;

import org.springframework.http.HttpStatus;

public class ErrorHttpStatusMapper {


    public static HttpStatus mapToStatus(ErrorCode errorCode) {

        switch (errorCode) {
            case BAD_REQUEST:
                return HttpStatus.BAD_REQUEST;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}
