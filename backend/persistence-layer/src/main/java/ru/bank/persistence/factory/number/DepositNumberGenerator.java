package ru.bank.persistence.factory.number;

public interface DepositNumberGenerator {
    String generateCreditCardNumber();
    String generateDebitCardNumber();
    String generateDepositNumber();
}
