package ru.bank.application.api.data.transfer;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class TransferExecuteApiRequest {
    @Min(value = 1)
    private long transferId;
    @Min(value = 1)
    private long debitCardId;
}
