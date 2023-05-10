package ru.bank.application.dto.card;


public class DebitCardDto extends CardDto {
    public DebitCardDto(long id,
                        double balance,
                        String cardNumber,
                        String closeDate
    ) {
        super(id, balance, cardNumber, "Debit", closeDate);
    }
}
