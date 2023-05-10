package ru.bank.application.service.cards.credit.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.repository.card.CreditCardRepository;

@Service
public class RejectCreditCardStateChanger implements CreditCardStateChanger {
    @Autowired
    CreditCardRepository creditCardRepository;


    @Override
    public void changeState(CreditCard card, String state) {
        card.setState(CardState.REJECTED);
        creditCardRepository.save(card);
    }

    @Override
    public void defineState(CreditCard card, String state) {
        if (state.equals(CardState.REJECTED.name())) {
            changeState(card, state);
        } else {
            throw new RuntimeException("State указан неверно");
        }
    }
}
