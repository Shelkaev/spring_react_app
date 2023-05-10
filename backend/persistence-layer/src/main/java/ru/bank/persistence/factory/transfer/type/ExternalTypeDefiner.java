package ru.bank.persistence.factory.transfer.type;

public class ExternalTypeDefiner implements TransferTypeDefiner {
    private String recipientAccountNumber;

    public ExternalTypeDefiner(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber.replaceAll(" ", "");
    }

    @Override
    public TransferTypeDefiner defineTransferType() {
        if (recipientAccountNumber.length() != 16 ){
            if (recipientAccountNumber.length() != 20 ){
                throw new RuntimeException("Номер счета имеет некорректный формат");
            }
        }
        return this;
    }

    @Override
    public TransferType outputTransferType() {
        return TransferType.EXTERNAL;
    }

}
