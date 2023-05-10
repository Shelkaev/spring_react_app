package ru.bank.application.dto.credit;

import lombok.Data;

@Data
public class CreditDto {

    private double creditPercent;
    private double penniPercent;
    private String dateRepayment;
    private double amount;
    private String currency;
    private double duty;

    public CreditDto(double creditPercent,
                     double penniPercent,
                     String dateRepayment,
                     double amount, String currency,
                     double duty) {
        this.creditPercent = creditPercent;
        this.penniPercent = penniPercent;
        this.dateRepayment = dateRepayment;
        this.amount = amount;
        this.currency = currency;
        this.duty = duty;
    }
}
