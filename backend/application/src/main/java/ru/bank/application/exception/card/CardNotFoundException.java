package ru.bank.application.exception.card;

import org.springframework.http.HttpStatus;
import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CardNotFoundException extends BaseException {
    public CardNotFoundException() {
        super(NOT_FOUND, "Не найдено ни одной карты");
    }
}
