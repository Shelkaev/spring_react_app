package ru.bank.application.dto.deposit;

import lombok.Data;


@Data
public class DepositDto {
    private long rateId;
    private String rateName;
    private double maxAmount;
    private double minAmount;
    private double percent;
    private boolean hasEarlyClosed;
    private  boolean hasIncreased;
    private boolean hasCapitalized;
    private boolean hasWithdrawal;
    private String accountNumber;
    private long depositId;
    private double balance;
    private String closeDate;

    public DepositDto(long rateId,
                      String rateName,
                      double maxAmount,
                      double minAmount,
                      double percent,
                      boolean hasEarlyClosed,
                      boolean hasIncreased,
                      boolean hasCapitalized,
                      boolean hasWithdrawal,
                      String accountNumber,
                      long depositId,
                      double balance,
                      String closeDate) {
        this.rateId = rateId;
        this.rateName = rateName;
        this.maxAmount = maxAmount;
        this.minAmount = minAmount;
        this.percent = percent;
        this.hasEarlyClosed = hasEarlyClosed;
        this.hasIncreased = hasIncreased;
        this.hasCapitalized = hasCapitalized;
        this.hasWithdrawal = hasWithdrawal;
        this.accountNumber = accountNumber;
        this.depositId = depositId;
        this.balance = balance;
        this.closeDate = closeDate;
    }
}
