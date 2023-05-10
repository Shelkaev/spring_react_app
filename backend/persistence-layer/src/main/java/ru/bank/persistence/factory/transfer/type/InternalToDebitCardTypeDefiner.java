package ru.bank.persistence.factory.transfer.type;

public class InternalToDebitCardTypeDefiner implements TransferTypeDefiner {
    private String recipientAccountNumber;

    public InternalToDebitCardTypeDefiner(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber.replaceAll(" ", "");
    }


    @Override
    public TransferTypeDefiner defineTransferType() {
        return isInternalAccount() ? this : new InternalToCreditCardTypeDefiner(recipientAccountNumber).defineTransferType();
    }

    @Override
    public TransferType outputTransferType() {
        return TransferType.INTERNAL_TO_DEBIT_CARD;
    }

    private boolean isInternalAccount() {
        return recipientAccountNumber.startsWith("11112") && recipientAccountNumber.length() == 16;
    }
}
