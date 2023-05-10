package ru.bank.persistence.repository.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    Optional<List<CreditCard>> findByUser(User user);
    Optional<CreditCard> findById(Long id);
    Optional<CreditCard> findByCardNumber(String cardNumber);
    @Query(value = "SELECT * FROM credit_card WHERE state LIKE 'ORDERED'", nativeQuery = true)
    Optional<List<CreditCard>> findOrderedCardsNative();

    @Query(value = "SELECT * FROM credit_card WHERE note LIKE 'changePenni'", nativeQuery = true)
    Optional<List<CreditCard>> findChangePenniCardsNative();
}
