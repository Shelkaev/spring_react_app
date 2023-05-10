package ru.bank.application.api.data.transfer;

import lombok.Data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Data
public class TransferApiRequest {
    @Min(value = 1)
    private double amount;
    private long cardId;
    @NotBlank
    private String beneficiaryAccount;
}
