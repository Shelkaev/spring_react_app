package ru.bank.persistence.entity.card;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@MappedSuperclass
public abstract class Card {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double balance;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;
    @Column(unique = true, nullable = false, length = 16)
    private String cardNumber;
    @Column(nullable = false)
    private LocalDateTime closeDate;

}
