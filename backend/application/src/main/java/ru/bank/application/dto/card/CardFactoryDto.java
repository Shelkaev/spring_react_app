package ru.bank.application.dto.card;

import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.card.DebitCard;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardFactoryDto {
    public CardFactoryDto() {
    }
    public static DebitCardDto crateDebitCard(DebitCard debitCard){
        return new DebitCardDto(
                debitCard.getId(),
                debitCard.getBalance(),
                muteCardNumber(debitCard.getCardNumber()),
                debitCard.getCloseDate().format(DateTimeFormatter.ofPattern("MM/yy"))
                );
    }

    public static List<DebitCardDto> createDebitCardList(List<DebitCard> cards){
        List<DebitCardDto> dtoCards = new ArrayList<>();
        for(DebitCard card : cards) {
            dtoCards.add(crateDebitCard(card));
        }
        return dtoCards;
    }

    public static CreditCardDto crateCreditCard(CreditCard creditCard){
        return new CreditCardDto(
                creditCard.getId(),
                creditCard.getBalance(),
                muteCardNumber(creditCard.getCardNumber()),
                creditCard.getCloseDate().format(DateTimeFormatter.ofPattern("MM/yy")),
                changeCardState(creditCard.getState()),
                creditCard.getCreditPercent(),
                creditCard.getPenniPercent(),
                creditCard.getCurrencyPercent(),
                creditCard.getNote(),
                creditCard.getLimit(),
                creditCard.getTotalDuty()
        );
    }


    public static List<CreditCardDto> createCreditCardList(List<CreditCard> cards){
        List<CreditCardDto> dtoCards = new ArrayList<>();
        for(CreditCard card : cards) {
            dtoCards.add(crateCreditCard(card));
        }
        return dtoCards;
    }

    public static OrderedCreditCardsDto crateOrderedCreditCard(CreditCard creditCard){
        return new OrderedCreditCardsDto(
                creditCard.getUser().getLogin(),
                creditCard.getId(),
                creditCard.getCardNumber()
        );
    }

    public static List<OrderedCreditCardsDto> createOrderedCreditCardsDtoList(List<CreditCard> cards){
        List<OrderedCreditCardsDto> dtoCards = new ArrayList<>();
        for(CreditCard card : cards) {
            dtoCards.add(crateOrderedCreditCard(card));
        }
        return dtoCards;
    }


    private static String muteCardNumber(String cardNumber){
        Matcher matcher = Pattern.compile("\\d{4}$").matcher(cardNumber);
        matcher.find();
        return "**** **** **** " + matcher.group();
    }

    public static String changeCardState(CardState state){
        if (state == CardState.ACTIVE){
            return "Активна";
        }
        if (state == CardState.ORDERED){
            return "На рассмотрении";
        }
        if (state == CardState.PROPOSED){
            return "Ожидает активации";
        }
        if (state == CardState.REJECTED){
            return "Отклонена";
        }
        return null;
    }
}
