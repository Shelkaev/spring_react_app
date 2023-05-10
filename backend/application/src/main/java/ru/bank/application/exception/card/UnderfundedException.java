package ru.bank.application.exception.card;

import org.springframework.http.HttpStatus;
import ru.bank.application.exception.BaseException;

public class UnderfundedException extends BaseException {
    public UnderfundedException() {
        super(HttpStatus.NOT_ACCEPTABLE, "Ожидайте утверждения карты");
    }
}
