package ru.bank.application.api.data.rate;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RateApiRequest {

    private long id;
    @NotBlank
    private String name;
    @NotNull
    private double maxAmount;
    @NotNull
    private double minAmount;
    @DecimalMax(value = "0.1")
    private double percent;
    @Min(value = 1)
    private int numberOfMonth;
    @NotNull
    private boolean hasEarlyClosed;
    @NotNull
    private  boolean hasIncreased;
    @NotNull
    private boolean hasCapitalized;
    @NotNull
    private boolean hasWithdrawal;
}
