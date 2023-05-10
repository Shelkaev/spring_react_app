package ru.bank.persistence.factory.cards.credit;

import java.time.LocalDateTime;

public interface CreditCardFactory {
    String getCardNumber();
    LocalDateTime getCloseDate();
    int getLimit();
    int getCreditPercent();

}
