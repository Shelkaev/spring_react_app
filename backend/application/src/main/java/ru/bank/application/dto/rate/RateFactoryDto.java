package ru.bank.application.dto.rate;

import ru.bank.persistence.entity.deposit.Rate;

import java.util.ArrayList;
import java.util.List;

public class RateFactoryDto {

    public RateFactoryDto() {

    }

    public static RateDto createRate(Rate rate) {
        return new RateDto(rate.getId(),
                rate.getName(),
                rate.getMaxAmount(),
                rate.getMinAmount(),
                rate.getPercent(),
                rate.getNumberOfMonth(),
                rate.getState().name(),
                rate.isHasEarlyClosed(),
                rate.isHasIncreased(),
                rate.isHasCapitalized(),
                rate.isHasWithdrawal());
    }

    public static List<RateDto> createRateList(List<Rate> rates) {
        List<RateDto> dtoRates = new ArrayList<>();
        for (Rate rate : rates) {
            dtoRates.add(createRate(rate));
        }
        return dtoRates;
    }

}
