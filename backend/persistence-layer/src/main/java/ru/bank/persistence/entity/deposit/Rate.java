package ru.bank.persistence.entity.deposit;

import javax.persistence.*;

@Entity
public class Rate {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private double maxAmount;
    @Column(nullable = false)
    private double minAmount;
    @Column(nullable = false)
    private double percent;
    @Column(nullable = false)
    private int numberOfMonth;
    @Enumerated(EnumType.STRING)
    private RateState state;
    private boolean hasEarlyClosed;
    private  boolean hasIncreased;
    private boolean hasCapitalized;
    private boolean hasWithdrawal;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public int getNumberOfMonth() {
        return numberOfMonth;
    }

    public void setNumberOfMonth(int numberOfMonth) {
        this.numberOfMonth = numberOfMonth;
    }

    public boolean isHasEarlyClosed() {
        return hasEarlyClosed;
    }

    public void setHasEarlyClosed(boolean hasEarlyClosed) {
        this.hasEarlyClosed = hasEarlyClosed;
    }

    public boolean isHasIncreased() {
        return hasIncreased;
    }

    public void setHasIncreased(boolean hasIncreased) {
        this.hasIncreased = hasIncreased;
    }

    public boolean isHasCapitalized() {
        return hasCapitalized;
    }

    public void setHasCapitalized(boolean hasCapitalized) {
        this.hasCapitalized = hasCapitalized;
    }

    public boolean isHasWithdrawal() {
        return hasWithdrawal;
    }

    public void setHasWithdrawal(boolean hasWithdrawal) {
        this.hasWithdrawal = hasWithdrawal;
    }

    public RateState getState() {
        return state;
    }

    public void setState(RateState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxAmount=" + maxAmount +
                ", minAmount=" + minAmount +
                '}';
    }
}
