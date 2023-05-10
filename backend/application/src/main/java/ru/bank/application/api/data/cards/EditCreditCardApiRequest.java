package ru.bank.application.api.data.cards;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditCreditCardApiRequest {
    @NotNull
    private long cardId;
    @NotBlank
    private String status;
}
