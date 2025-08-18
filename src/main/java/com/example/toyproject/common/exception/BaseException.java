package com.example.toyproject.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private final CustomErrorCode errorCode;

    public BaseException(CustomErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(CustomErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
