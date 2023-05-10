package ru.bank.application.exception.user;
import ru.bank.application.exception.BaseException;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CONFLICT;

public class UserExistException extends BaseException {
    public UserExistException() {
        super(CONFLICT, format("Логин занят"));
    }
}
