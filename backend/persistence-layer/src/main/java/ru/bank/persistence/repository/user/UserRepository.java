package ru.bank.persistence.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bank.persistence.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByName(String name);
}
