package ru.bank.application.exception.deposit;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class RateNotFoundException extends BaseException {
    public RateNotFoundException() {
        super(NOT_FOUND, "Тарифа не существует");
    }
}
