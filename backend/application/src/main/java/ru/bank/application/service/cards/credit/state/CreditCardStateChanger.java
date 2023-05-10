package ru.bank.application.service.cards.credit.state;

import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;

public interface CreditCardStateChanger {
    void changeState(CreditCard card, String state);
    void defineState(CreditCard card, String state);
}
