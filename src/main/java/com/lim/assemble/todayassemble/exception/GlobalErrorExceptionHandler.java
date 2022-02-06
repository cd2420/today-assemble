package com.lim.assemble.todayassemble.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalErrorExceptionHandler {

    @ExceptionHandler(TodayAssembleException.class)
    public ResponseEntity<ErrorResponse> responseError(
            TodayAssembleException ex
            , HttpServletRequest request
    ) {
        log.info("$$$$$$$$$ errorCode: {}, url: {}, message: {}"
                , ex.getErrorCode()
                , request.getRequestURI()
                , ex.getMessage());

        final ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                new ErrorResponse(errorCode, errorCode.getMessage())
                , ErrorHttpStatusMapper.mapToStatus(errorCode)
        );
    }

    /**
     * requestBody에서 annotation validate에 걸린 값 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> responseError(
            MethodArgumentNotValidException ex
            , HttpServletRequest request
    ) {
        String message =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(x -> x.getDefaultMessage())
                        .collect(Collectors.toList()).get(0);
        log.info("$$$$$$$$$ url: {}, message: {}"
                , request.getRequestURI()
                , message);

        final ErrorCode badRequest = ErrorCode.BAD_REQUEST;
        return new ResponseEntity<>(
                new ErrorResponse(badRequest, message)
                , ErrorHttpStatusMapper.mapToStatus(badRequest)
        );
    }

}
