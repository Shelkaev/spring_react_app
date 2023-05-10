package ru.bank.persistence.factory.transfer.type;

public class InternalToCreditCardTypeDefiner implements TransferTypeDefiner {
    private String recipientAccountNumber;

    public InternalToCreditCardTypeDefiner(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber.replaceAll(" ", "");
    }


    @Override
    public TransferTypeDefiner defineTransferType() {
        return isInternalAccount() ? this : new InternalToDepositTypeDefiner(recipientAccountNumber).defineTransferType();
    }

    @Override
    public TransferType outputTransferType() {
        return TransferType.INTERNAL_TO_CREDIT_CARD;
    }

    private boolean isInternalAccount() {
        return recipientAccountNumber.startsWith("11110") && recipientAccountNumber.length() == 16;
    }
}
