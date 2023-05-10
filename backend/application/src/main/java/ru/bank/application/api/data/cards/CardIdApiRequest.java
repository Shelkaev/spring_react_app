package ru.bank.application.api.data.cards;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CardIdApiRequest {
    @NotNull
    private long cardId;
}
