package ru.bank.persistence.entity.credit;

import ru.bank.persistence.entity.card.CreditCard;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Credit {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private double amount;
    private double amountRu;
    private LocalDateTime dateRepayment;
    private double duty;
    @ManyToOne
    @JoinColumn(name="credit_card_id")
    private CreditCard creditCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getDateRepayment() {
        return dateRepayment;
    }

    public void setDateRepayment(LocalDateTime dateRepayment) {
        this.dateRepayment = dateRepayment;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public double getDuty() {
        return duty;
    }

    public void setDuty(double duty) {
        this.duty = duty;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountRu() {
        return amountRu;
    }

    public void setAmountRu(double amountRu) {
        this.amountRu = amountRu;
    }


    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", currency=" + currency +
                ", duty=" + duty +
                '}';
    }
}
