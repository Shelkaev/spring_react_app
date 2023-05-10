package ru.bank.persistence.factory.transfer.type;

public class InternalToDepositTypeDefiner implements TransferTypeDefiner {
    private String recipientAccountNumber;

    public InternalToDepositTypeDefiner(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber.replaceAll(" ", "");
    }


    @Override
    public TransferTypeDefiner defineTransferType() {
        return isInternalAccount() ? this : new ExternalTypeDefiner(recipientAccountNumber).defineTransferType();
    }

    @Override
    public TransferType outputTransferType() {
        return TransferType.INTERNAL_TO_DEPOSIT;
    }

    private boolean isInternalAccount() {
        return  recipientAccountNumber.startsWith("44444") && recipientAccountNumber.length() == 20;
    }
}
