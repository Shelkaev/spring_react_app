package ru.bank.application.dto.credit;

import lombok.Data;

@Data
public class CreditDetailsDto {
    private long cardId;
    private String currency;
    private double currencyCommission;
    private double creditPercent;
    private String dateRepayment;
    private double amount;

    public CreditDetailsDto() {
    }

    public CreditDetailsDto(long cardId,
                            String currency,
                            double currencyCommission,
                            double creditPercent,
                            String dateRepayment,
                            double amount) {
        this.cardId = cardId;
        this.currency = currency;
        this.currencyCommission = currencyCommission;
        this.creditPercent = creditPercent;
        this.dateRepayment = dateRepayment;
        this.amount = amount;
    }
}
