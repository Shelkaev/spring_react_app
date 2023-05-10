package ru.bank.application.exception.transfer;

import ru.bank.application.exception.BaseException;

import static org.springframework.http.HttpStatus.LOCKED;

public class FraudMonitorException extends BaseException {
    public FraudMonitorException() {
        super(LOCKED, "Перевод не выполнен Причина: подозрительный перевод");
    }
}
