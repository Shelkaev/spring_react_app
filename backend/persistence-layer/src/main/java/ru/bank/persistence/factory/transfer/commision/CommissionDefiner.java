package ru.bank.persistence.factory.transfer.commision;

public interface CommissionDefiner {
    public CommissionDefiner defineCommission();
    public double outputCommission();
}
