package ru.bank.persistence.factory.transfer.commision;

import ru.bank.persistence.factory.transfer.type.TransferType;

public class CommissionFactory {
    public static double calculateTransferCommission(TransferType type){
        CommissionDefiner typeDefiner = new ExternalCommission(type);
        return typeDefiner.defineCommission().outputCommission();
    }
}
