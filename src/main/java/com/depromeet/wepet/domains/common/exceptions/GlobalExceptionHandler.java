package com.depromeet.wepet.domains.common.exceptions;

import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.respose.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    protected ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException e) {
        BindingResult bindingResult = e.getBindingResult();
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, bindingResult);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // BusinessException
    @ExceptionHandler(WepetException.class)
    protected ResponseEntity<ErrorResponse> handleWepetException(WepetException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode(), e.getHttpStatus(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
