package ru.bank.application.service.cards.debit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.exception.card.UnderfundedException;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.debit.DebitCardFactory;
import ru.bank.persistence.repository.card.DebitCardRepository;

import java.util.List;

@Service
public class DebitCardService {
    @Autowired
    private DebitCardRepository debitCardRepository;

    public DebitCard createDebitCard(User user, DebitCardFactory debitCardFactory) {
        DebitCard debitCard = new DebitCard(debitCardFactory);
        debitCard.setUser(user);
        debitCardRepository.save(debitCard);
        return debitCard;
    }

    public List<DebitCard> findByUser(User user) {
        return debitCardRepository.findByUser(user).orElseThrow(CardNotFoundException::new);
    }

    public DebitCard findById(Long id) {
        return debitCardRepository.findById(id).orElseThrow(CardNotFoundException::new);
    }

    public void increaseBalance(DebitCard card, double amount) {
        card.setBalance(card.getBalance() + amount);
        debitCardRepository.save(card);
    }

    public void decreaseBalance(DebitCard card, double amount) {
        if (card.getBalance() < amount) {
            throw new UnderfundedException();
        }
        card.setBalance(card.getBalance() - amount);
        debitCardRepository.save(card);
    }

    public DebitCard getUserCardById(User user, Long cardId) {
        return findByUser(user)
                .stream()
                .filter(c -> c.getId() == cardId)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
    }

}
