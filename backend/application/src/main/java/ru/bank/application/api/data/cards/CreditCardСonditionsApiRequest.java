package ru.bank.application.api.data.cards;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CreditCardСonditionsApiRequest {
    @Min(value = 1000)
    private double limit;
    @NotNull
    private double creditPercent;
    @NotNull
    private double currencyPercent;
    @NotNull
    private double penniPercent;
    @NotNull
    private long cardId;

}
