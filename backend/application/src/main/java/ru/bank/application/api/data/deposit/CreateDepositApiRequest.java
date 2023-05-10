package ru.bank.application.api.data.deposit;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateDepositApiRequest {
    @NotNull
    private long rateId;

}
