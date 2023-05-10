package ru.bank.application.dto.card;

import lombok.Data;
import ru.bank.persistence.entity.card.CardType;
import java.time.LocalDateTime;

@Data
public abstract class CardDto {
    private long id;
    private double balance;
    private String cardNumber;
    private String cardType;
    private String closeDate;


    public CardDto() {
    }

    public CardDto(long id,
                   double balance,
                   String cardNumber,
                   String cardType,
                   String closeDate
) {
        this.id = id;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.closeDate = closeDate;

    }
}
