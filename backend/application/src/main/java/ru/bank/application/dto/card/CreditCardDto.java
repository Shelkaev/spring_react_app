package ru.bank.application.dto.card;

import lombok.Data;

@Data
public class CreditCardDto extends CardDto {
    private String cardState;
    private double creditPercent;
    private double penniPercent;
    private double currencyPercent;
    private String note;
    private double limit;
    private double totalDuty;

    public CreditCardDto(long id,
                         double balance,
                         String cardNumber,
                         String closeDate,
                         String cardState,
                         double creditPercent,
                         double penniPercent,
                         double currencyPercent,
                         String note,
                         double limit,
                         double totalDuty
                         ) {
        super(id, balance, cardNumber, "Credit", closeDate);
        this.cardState = cardState;
        this.creditPercent = creditPercent;
        this.penniPercent = penniPercent;
        this.currencyPercent = currencyPercent;
        this.note = note;
        this.limit = limit;
        this.totalDuty = totalDuty;
    }

}
