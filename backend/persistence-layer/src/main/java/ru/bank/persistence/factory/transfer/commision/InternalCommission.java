package ru.bank.persistence.factory.transfer.commision;

import ru.bank.persistence.factory.transfer.type.TransferType;

public class InternalCommission implements CommissionDefiner{
    private final TransferType type;
    private final double commission = 0;

    public InternalCommission(TransferType type) {
        this.type = type;
    }

    @Override
    public CommissionDefiner defineCommission() {
        return this;
    }

    @Override
    public double outputCommission() {
        return commission;
    }
}
