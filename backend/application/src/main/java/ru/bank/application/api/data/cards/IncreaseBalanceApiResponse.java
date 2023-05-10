package ru.bank.application.api.data.cards;

import lombok.Data;

@Data
public class IncreaseBalanceApiResponse {
    double balance;

    public IncreaseBalanceApiResponse(double balance) {
        this.balance = balance;
    }
}
