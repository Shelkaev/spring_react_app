package ru.bank.application.api.data.rate;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RateIdApiRequest {
    @NotNull
    private long id;
}
