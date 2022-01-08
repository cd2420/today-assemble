package com.lim.assemble.todayassemble.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalErrorExceptionHandler {

    @ExceptionHandler(TodayAssembleException.class)
    public ResponseEntity<ErrorResponse> responseError(
            TodayAssembleException ex
            , HttpServletRequest request
    ) {
        log.info("errorCode: {}, url: {}, message: {}"
                , ex.getErrorCode()
                , request.getRequestURI()
                , ex.getMessage());

        final ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ErrorResponse(errorCode, errorCode.getMessage())
                , ErrorHttpStatusMapper.mapToStatus(errorCode)
        );
    }

}
