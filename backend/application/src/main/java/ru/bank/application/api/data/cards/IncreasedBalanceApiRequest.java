package ru.bank.application.api.data.cards;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class IncreasedBalanceApiRequest {
    @Min(value = 1)
    private double amount;
    @NotNull
    private long cardId;
}
