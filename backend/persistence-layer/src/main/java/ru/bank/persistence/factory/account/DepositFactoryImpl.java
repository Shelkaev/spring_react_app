package ru.bank.persistence.factory.account;


import ru.bank.persistence.factory.number.DepositNumberGeneratorImpl;

public class DepositFactoryImpl implements DepositFactory {

    @Override
    public String getDepositNumber() {
        DepositNumberGeneratorImpl accountNumberGenerator = new DepositNumberGeneratorImpl();
        return accountNumberGenerator.generateDepositNumber();
    }
}
