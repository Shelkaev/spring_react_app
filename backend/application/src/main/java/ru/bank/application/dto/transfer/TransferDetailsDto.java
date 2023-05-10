package ru.bank.application.dto.transfer;

import lombok.Data;

@Data
public class TransferDetailsDto {
    private long id;
    private long debitCardId;
    private String type;
    private String date;
    private String status;
    private double commission;
    private double amount;
    private String account;
    private String recipientAccount;

    public TransferDetailsDto(long id,
                              long debitCardId,
                              String type,
                              String date,
                              String status,
                              double commission,
                              double amount,
                              String account,
                              String recipientAccount) {
        this.id = id;
        this.debitCardId = debitCardId;
        this.type = type;
        this.date = date;
        this.status = status;
        this.commission = commission;
        this.amount = amount;
        this.account = account;
        this.recipientAccount = recipientAccount;
    }
}
