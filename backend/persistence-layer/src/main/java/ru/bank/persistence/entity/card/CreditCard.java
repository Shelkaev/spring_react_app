package ru.bank.persistence.entity.card;

import lombok.extern.slf4j.Slf4j;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;

import javax.persistence.*;


@Entity
@Slf4j
public class CreditCard extends Card{

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardState state;
    @Column(name = "lim")
    private double limit;
    private double totalDuty;
    @Column(nullable = false)
    private double creditPercent;
    private double penniPercent;
    private double currencyPercent;
    private String note;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;


    public CreditCard() {
        setCardType(CardType.CREDIT);
    }

    public CreditCard(CreditCardFactory creditCardFactory) {
        setCardType(CardType.CREDIT);
        setCardNumber(creditCardFactory.getCardNumber());
        setCloseDate(creditCardFactory.getCloseDate());
        this.state = CardState.ORDERED;
        setBalance(creditCardFactory.getLimit());
        this.limit = creditCardFactory.getLimit();
        this.creditPercent = creditCardFactory.getCreditPercent();
    }



    public CardState getState() {
        return state;
    }

    public void setState(CardState state) {
        this.state = state;
    }

    public double getCreditPercent() {
        return creditPercent;
    }

    public void setCreditPercent(double creditPercent) {
        this.creditPercent = creditPercent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getPenniPercent() {
        return penniPercent;
    }

    public void setPenniPercent(double penniPercent) {
        this.penniPercent = penniPercent;
    }

    public double getCurrencyPercent() {
        return currencyPercent;
    }

    public void setCurrencyPercent(double currencyPercent) {
        this.currencyPercent = currencyPercent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotalDuty() {
        return totalDuty;
    }

    public void setTotalDuty(double totalDuty) {
        this.totalDuty = totalDuty;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardNumber=" + getCardNumber() +
                "cardType=" + getCardType() +
                '}';
    }
}
