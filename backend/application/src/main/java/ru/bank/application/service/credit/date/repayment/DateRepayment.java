package ru.bank.application.service.credit.date.repayment;

import java.time.LocalDateTime;

public class DateRepayment {
    public static LocalDateTime define(LocalDateTime closeDate) {
        if (closeDate.isBefore(LocalDateTime.now().plusDays(30))
                && closeDate.isAfter(LocalDateTime.now().plusDays(1))) {
            return closeDate;
        }
        if (closeDate.isBefore(LocalDateTime.now().plusDays(1))) {
            throw new RuntimeException("Отказано. Действие карты заканчивается в течении 1 дня.");
        }
        return LocalDateTime.now().plusDays(30);
    }
}
