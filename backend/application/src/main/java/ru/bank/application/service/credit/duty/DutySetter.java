package ru.bank.application.service.credit.duty;

import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.persistence.entity.credit.Credit;

public interface DutySetter {
    void setDuty(CreditDetailsDto request, Credit credit);
    void defineCurrency(CreditDetailsDto request, Credit credit);
}
