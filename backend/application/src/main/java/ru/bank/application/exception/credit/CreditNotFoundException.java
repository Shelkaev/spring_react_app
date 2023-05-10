package ru.bank.application.exception.credit;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CreditNotFoundException extends BaseException {
    public CreditNotFoundException() {
        super(NOT_FOUND, "Кредиты не найдены");
    }
}
