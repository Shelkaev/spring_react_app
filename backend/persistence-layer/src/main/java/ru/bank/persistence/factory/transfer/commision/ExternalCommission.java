package ru.bank.persistence.factory.transfer.commision;

import ru.bank.persistence.factory.transfer.type.TransferType;

public class ExternalCommission implements CommissionDefiner{
    private final TransferType type;
    private final double commission = 0.01;

    public ExternalCommission(TransferType type) {
        this.type = type;
    }

    @Override
    public CommissionDefiner defineCommission() {
        return type == TransferType.EXTERNAL ? this : new InternalCommission(type);
    }

    @Override
    public double outputCommission() {
        return commission;
    }
}
