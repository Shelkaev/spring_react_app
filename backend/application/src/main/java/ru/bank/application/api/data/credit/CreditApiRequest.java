package ru.bank.application.api.data.credit;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreditApiRequest {
    @Min(value = 1)
    private double amount;
    @NotNull
    private long cardId;
    @NotBlank
    private String currency;
}
