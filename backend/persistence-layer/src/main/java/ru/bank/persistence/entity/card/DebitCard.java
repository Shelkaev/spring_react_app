package ru.bank.persistence.entity.card;

import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.debit.DebitCardFactory;

import javax.persistence.*;


@Entity
@Table(name = "debit_card")
public class DebitCard extends Card{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public DebitCard() {
        setCardType(CardType.DEBIT);
    }

    public DebitCard(DebitCardFactory debitCardFactory) {
        setCardType(CardType.DEBIT);
        setCardNumber(debitCardFactory.getCardNumber());
        setCloseDate(debitCardFactory.getCloseDate());
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public String toString() {
        return "DebitCard{" +
                "cardNumber=" + getCardNumber() +
                "cardType=" + getCardType() +
                '}';
    }
}
