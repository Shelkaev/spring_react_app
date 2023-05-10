package ru.bank.persistence.factory.cards.debit;


import ru.bank.persistence.factory.date.DateSupplier;
import ru.bank.persistence.factory.date.DateSupplierImpl;
import ru.bank.persistence.factory.number.DepositNumberGeneratorImpl;

import java.time.LocalDateTime;

public class DebitCardFactoryStandard implements DebitCardFactory{
    private final DateSupplier dateSupplier = new DateSupplierImpl();

    @Override
    public String getCardNumber() {
        DepositNumberGeneratorImpl accountNumberGenerator = new DepositNumberGeneratorImpl();
        return accountNumberGenerator.generateDebitCardNumber();
    }

    @Override
    public LocalDateTime getCloseDate() {
        return dateSupplier.getDebitCardCloseDate();
    }
}
