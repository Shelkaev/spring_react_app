package ru.bank.persistence.factory.cards.credit;


import ru.bank.persistence.factory.date.DateSupplier;
import ru.bank.persistence.factory.date.DateSupplierImpl;
import ru.bank.persistence.factory.number.DepositNumberGeneratorImpl;

import java.time.LocalDateTime;


public class CreditCardFactoryStandard implements CreditCardFactory{

    @Override
    public String getCardNumber() {
        DepositNumberGeneratorImpl accountNumberGenerator = new DepositNumberGeneratorImpl();
        return accountNumberGenerator.generateCreditCardNumber();
    }

    @Override
    public LocalDateTime getCloseDate() {
        DateSupplier dateSupplier = new DateSupplierImpl();
        return dateSupplier.getCreditCardCloseDate();
    }

    @Override
    public int getLimit() {
        return 0;
    }

    @Override
    public int getCreditPercent() {
        return 0;
    }


}
