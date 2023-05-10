package ru.bank.application.exception.transfer;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class RecipientAccountNotFoundException extends BaseException {
    public RecipientAccountNotFoundException() {
        super(NOT_FOUND, "Получатель не найден");
    }
}
