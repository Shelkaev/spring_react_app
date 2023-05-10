package ru.bank.application.exception.user;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(NOT_FOUND, "Пользователь не найден");
    }
}
