package ru.bank.application.dto.credit;

import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreditDtoFactory {

    public static CreditDto createCreditDto(CreditCard card, Credit credit) {
        return new CreditDto(card.getCreditPercent(),
                card.getPenniPercent(),
                setDateOrEmpty(credit.getDateRepayment()),
                credit.getAmount(),
                credit.getCurrency().name(),
                credit.getDuty()
                );
    }

    public static List<CreditDto> createCreditDtoList(List<Credit> credits, CreditCard card) {
        List<CreditDto> dtoCredits = new ArrayList<>();
        for(Credit credit : credits) {
            dtoCredits.add(createCreditDto(card, credit));
        }
        return dtoCredits;
    }


    public static String setDateOrEmpty(LocalDateTime date){
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

}
