package ru.bank.application.service.credit.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.service.credit.currency.CurrencyValue;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;
import ru.bank.persistence.entity.credit.Currency;

@Service
public class EurDutySetter implements DutySetter {
    @Autowired
    UsdDutySetter usdDutySetter;

    @Override
    public void setDuty(CreditDetailsDto request, Credit credit) {
        double amountRu = request.getAmount() * CurrencyValue.getEur();
        validateAmount(credit.getCreditCard(), amountRu);
        double currencyPercent = credit.getCreditCard().getCurrencyPercent();
        double creditPercent = credit.getCreditCard().getCreditPercent();
        credit.setCurrency(Currency.EUR);
        credit.setAmount(request.getAmount());
        credit.setAmountRu(amountRu);
        credit.setDuty(amountRu + (amountRu * currencyPercent) + (amountRu * creditPercent));
    }

    @Override
    public void defineCurrency(CreditDetailsDto request, Credit credit) {
        if (request.getCurrency().equals("EUR")) {
            setDuty(request, credit);
        } else {
            usdDutySetter.defineCurrency(request, credit);
        }
    }
    private void validateAmount(CreditCard card, double amountRu) {
        if (amountRu + (amountRu * card.getCreditPercent())
                + (amountRu * card.getCurrencyPercent()) > (card.getLimit() - card.getTotalDuty())) {
            throw new RuntimeException("Недостаточно средств");
        }
    }
}
