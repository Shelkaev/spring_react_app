package ru.bank.application.service.credit.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;
import ru.bank.persistence.entity.credit.Currency;

@Service
public class RuDutySetter implements DutySetter {
    @Autowired
    EurDutySetter eurDutySetter;

    @Override
    public void setDuty(CreditDetailsDto request, Credit credit) {
        double creditPercent = credit.getCreditCard().getCreditPercent();
        validateAmount(credit.getCreditCard(), request.getAmount());
        credit.setCurrency(Currency.RU);
        credit.setAmount(request.getAmount());
        credit.setAmountRu(request.getAmount());
        credit.setDuty(request.getAmount() + (request.getAmount() * creditPercent));
    }

    @Override
    public void defineCurrency(CreditDetailsDto request, Credit credit) {
        if (request.getCurrency().equals("RU")) {
            setDuty(request, credit);
        } else {
            eurDutySetter.defineCurrency(request, credit);
        }
    }

    private void validateAmount(CreditCard card, double amount) {
        if (amount + (amount * card.getCreditPercent()) > (card.getLimit() - card.getTotalDuty())) {
            throw new RuntimeException("Недостаточно средств");
        }
    }
}
