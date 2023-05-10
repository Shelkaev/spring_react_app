package ru.bank.application.api.data.deposit;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DecreaseDepositApiRequest {
    @NotNull
    private long debitCardId;
    @NotNull
    private long depositId;
    @Min(value = 1)
    private double amount;

}
