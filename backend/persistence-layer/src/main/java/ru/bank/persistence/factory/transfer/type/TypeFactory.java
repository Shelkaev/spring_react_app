package ru.bank.persistence.factory.transfer.type;

public class TypeFactory {

    public static TransferType getType(String recipientAccountNumber) {

        TransferTypeDefiner typeDefiner = new InternalToDebitCardTypeDefiner(recipientAccountNumber);
        return typeDefiner.defineTransferType().outputTransferType();
    }
}
