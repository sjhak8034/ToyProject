package com.example.toyproject.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {

    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드 실패"), BAD_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 포맷입니다" );


    private final HttpStatus httpStatus;
    private final String message;

    CustomErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
