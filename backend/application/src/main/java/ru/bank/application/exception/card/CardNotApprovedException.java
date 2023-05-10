package ru.bank.application.exception.card;

import org.springframework.http.HttpStatus;
import ru.bank.application.exception.BaseException;

public class CardNotApprovedException extends BaseException {
    public CardNotApprovedException() {
        super(HttpStatus.FORBIDDEN, "Ожидайте утверждения карты");
    }
}
