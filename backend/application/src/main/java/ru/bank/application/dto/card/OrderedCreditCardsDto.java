package ru.bank.application.dto.card;

import lombok.Data;

@Data
public class OrderedCreditCardsDto {
    private String userLogin;
    private long cardId;
    private String cardNumber;

    public OrderedCreditCardsDto(String userLogin, long cardId, String cardNumber) {
        this.userLogin = userLogin;
        this.cardId = cardId;
        this.cardNumber = cardNumber;
    }
}
