package ru.bank.persistence.factory.transfer.type;

public interface TransferTypeDefiner {
    public TransferTypeDefiner defineTransferType();
    public TransferType outputTransferType();
}
