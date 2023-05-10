package ru.bank.application.api.data.cards;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PenniApiRequest {
    @NotNull
    private double penniPercent;
    @NotNull
    private double currencyPercent;
    @NotNull
    private long cardId;
}
