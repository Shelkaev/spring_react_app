package ru.bank.persistence.repository.deposit;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.persistence.entity.deposit.Rate;
import java.util.Optional;


public interface RateRepository extends JpaRepository<Rate, Long> {
    Optional<Rate> findByName(String name);
    Optional<Rate> findById(long id);
}
