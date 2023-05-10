package ru.bank.application.exception.transfer;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class TransferNotFoundException extends BaseException {
    public TransferNotFoundException() {
        super(NOT_FOUND, "Не найдено ни одного перевода");
    }
}
