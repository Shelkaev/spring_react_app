package ru.bank.application.dto.rate;

import lombok.Data;

@Data
public class RateDto {
    private long id;
    private String name;
    private double maxAmount;
    private double minAmount;
    private double percent;
    private int numberOfMonth;
    private String state;
    private boolean hasEarlyClosed;
    private boolean hasIncreased;
    private boolean hasCapitalized;
    private boolean hasWithdrawal;

    public RateDto(long id,
                   String name,
                   double maxAmount,
                   double minAmount,
                   double percent,
                   int numberOfMonth,
                   String state,
                   boolean hasEarlyClosed,
                   boolean hasIncreased,
                   boolean hasCapitalized,
                   boolean hasWithdrawal) {
        this.id = id;
        this.name = name;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.percent = percent;
        this.numberOfMonth = numberOfMonth;
        this.state = state;
        this.hasEarlyClosed = hasEarlyClosed;
        this.hasIncreased = hasIncreased;
        this.hasCapitalized = hasCapitalized;
        this.hasWithdrawal = hasWithdrawal;
    }
}
