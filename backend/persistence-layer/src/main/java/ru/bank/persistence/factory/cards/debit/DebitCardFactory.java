package ru.bank.persistence.factory.cards.debit;

import java.time.LocalDateTime;

public interface DebitCardFactory {
    String getCardNumber();
    LocalDateTime getCloseDate();
}
