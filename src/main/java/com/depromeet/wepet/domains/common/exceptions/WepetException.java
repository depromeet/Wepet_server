package com.depromeet.wepet.domains.common.exceptions;

import com.depromeet.wepet.domains.common.constans.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WepetException extends RuntimeException{

    private ErrorCode errorCode;
    private HttpStatus httpStatus;
    private String message;

    public WepetException(ErrorCode errorCode, HttpStatus httpStatus, String message) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
