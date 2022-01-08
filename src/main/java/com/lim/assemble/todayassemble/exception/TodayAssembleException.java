package com.lim.assemble.todayassemble.exception;

import lombok.Getter;

@Getter
public class TodayAssembleException extends RuntimeException {

    private ErrorCode errorCode;

    public TodayAssembleException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public TodayAssembleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
