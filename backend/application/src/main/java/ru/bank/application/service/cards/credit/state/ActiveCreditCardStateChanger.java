package ru.bank.application.service.cards.credit.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.repository.card.CreditCardRepository;

@Service
public class ActiveCreditCardStateChanger implements CreditCardStateChanger{
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    RejectCreditCardStateChanger rejectCreditCardStateChanger;

    @Override
    public void changeState(CreditCard card, String state) {
        card.setState(CardState.ACTIVE);
        creditCardRepository.save(card);
    }

    @Override
    public void defineState(CreditCard card, String state) {
        validateCreditCardState(card);
        if (state.equals(CardState.ACTIVE.name())) {
            changeState(card, state);
        } else {
            rejectCreditCardStateChanger.defineState(card, state);
        }
    }

    private void validateCreditCardState(CreditCard card) {
        if (card.getState() == CardState.ACTIVE) {
            throw new RuntimeException("Карта уже активирована");
        }
        if (card.getState() == CardState.ORDERED) {
            throw new RuntimeException("Карта находится на подтверждении оператора. Дождитесь проверки");
        }
    }
}
