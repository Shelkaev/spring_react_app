package ru.bank.application.api.data.deposit;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CloseDepositApiRequest {
    @NotNull
    private long debitCardId;
    @NotNull
    private long depositId;

}
