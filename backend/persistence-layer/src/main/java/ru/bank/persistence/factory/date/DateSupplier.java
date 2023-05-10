package ru.bank.persistence.factory.date;

import java.time.LocalDateTime;

public interface DateSupplier {
    LocalDateTime getCreditCardCloseDate();
    LocalDateTime getDebitCardCloseDate();
}
