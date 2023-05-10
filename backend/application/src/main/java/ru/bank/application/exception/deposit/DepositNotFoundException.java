package ru.bank.application.exception.deposit;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class DepositNotFoundException extends BaseException {
    public DepositNotFoundException() {
        super(NOT_FOUND, "Вклада не существует");
    }
}
