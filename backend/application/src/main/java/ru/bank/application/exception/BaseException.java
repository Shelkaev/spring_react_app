package ru.bank.application.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;

    public BaseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
