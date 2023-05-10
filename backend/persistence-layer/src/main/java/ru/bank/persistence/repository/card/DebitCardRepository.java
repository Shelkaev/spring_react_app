package ru.bank.persistence.repository.card;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface DebitCardRepository extends JpaRepository<DebitCard, Long> {
    Optional<List<DebitCard>> findByUser(User user);
    Optional<DebitCard> findById(Long id);
    Optional<DebitCard> findByCardNumber(String cardNumber);
}
