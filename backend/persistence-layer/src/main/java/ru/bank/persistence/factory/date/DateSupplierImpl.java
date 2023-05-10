package ru.bank.persistence.factory.date;


import java.time.LocalDateTime;

public class DateSupplierImpl implements DateSupplier{
    @Override
    public LocalDateTime getCreditCardCloseDate() {
        return LocalDateTime.now().plusYears(3);
    }

    @Override
    public LocalDateTime getDebitCardCloseDate() {
        return LocalDateTime.now().plusYears(3);
    }
}
