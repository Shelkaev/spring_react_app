package ru.bank.persistence.repository.deposit;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.persistence.entity.deposit.Deposit;
import ru.bank.persistence.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    Optional<List<Deposit>> findByUser(User user);
    Optional<Deposit> findById(Long id);
    Optional<Deposit> findByDepositNumber(String depositNumber);
}
