package com.hanghae99plus.duhee.lecture_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request 반환
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
