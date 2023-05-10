package ru.bank.persistence.entity.deposit;


import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.account.DepositFactory;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Deposit {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double balance;
    private double ownMoney;
    @Column(unique = true, nullable = false)
    private String depositNumber;
    private LocalDateTime closeDate;

    public Deposit() {
    }

    public Deposit(DepositFactory depositFactory) {
        this.depositNumber = depositFactory.getDepositNumber();
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="rate_id")
    private Rate rate;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDepositNumber() {
        return depositNumber;
    }

    public void setDepositNumber(String depositNumber) {
        this.depositNumber = depositNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getOwnMoney() {
        return ownMoney;
    }

    public void setOwnMoney(double ownMoney) {
        this.ownMoney = ownMoney;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public LocalDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", accountNumber='" + depositNumber + '\'' +
                '}';
    }
}
