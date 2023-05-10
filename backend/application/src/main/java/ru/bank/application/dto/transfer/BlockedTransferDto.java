package ru.bank.application.dto.transfer;

import lombok.Data;

@Data
public class BlockedTransferDto {
    private long transferId;
    private String recipientAccount;
    private String senderAccount;
    private String date;
    private String senderLogin;
    private double amount;

    public BlockedTransferDto(long transferId,
                              String recipientAccount,
                              String senderAccount,
                              String date,
                              String senderLogin,
                              double amount) {
        this.transferId = transferId;
        this.recipientAccount = recipientAccount;
        this.senderAccount = senderAccount;
        this.date = date;
        this.senderLogin = senderLogin;
        this.amount = amount;
    }
}
