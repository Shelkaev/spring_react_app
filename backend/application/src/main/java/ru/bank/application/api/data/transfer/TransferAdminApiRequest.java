package ru.bank.application.api.data.transfer;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransferAdminApiRequest {
    @NotNull
    private long transferId;
}
