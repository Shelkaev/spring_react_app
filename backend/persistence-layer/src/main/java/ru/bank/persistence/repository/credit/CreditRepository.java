package ru.bank.persistence.repository.credit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;

import java.util.List;
import java.util.Optional;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<List<Credit>> findAllByCreditCard(CreditCard card);

    @Query(value = "SELECT * FROM credit WHERE duty > 0", nativeQuery = true)
    Optional<List<Credit>> findAllActiveNative();

}
