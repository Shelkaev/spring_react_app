package ru.bank.application.service.deposit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.exception.deposit.DepositNotFoundException;
import ru.bank.application.exception.deposit.RateNotFoundException;
import ru.bank.application.exception.user.UserNotFoundException;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.deposit.Deposit;
import ru.bank.persistence.entity.deposit.Rate;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.account.DepositFactoryImpl;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.deposit.DepositRepository;
import ru.bank.persistence.repository.deposit.RateRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepositService {
    @Autowired
    private RateRepository rateRepository;
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private DebitCardRepository debitCardRepository;

    public List<Deposit> getDeposits(User user) {
        return depositRepository.findByUser(user).orElseThrow(DepositNotFoundException::new)
                .stream()
                .filter(d -> LocalDateTime.now().isBefore(d.getCloseDate()))
                .collect(Collectors.toList());
    }

    public Deposit getOneDeposit(User user, long depositId) {
        return depositRepository.findByUser(user).orElseThrow(DepositNotFoundException::new)
                .stream()
                .filter(d -> LocalDateTime.now().isBefore(d.getCloseDate()))
                .filter(d -> d.getId() == depositId)
                .findFirst().orElseThrow(DepositNotFoundException::new);
    }

    public void createDeposit(User user, long rateId) {
        Rate rate = rateRepository.findById(rateId).orElseThrow(RateNotFoundException::new);
        DepositFactoryImpl depositFactory = new DepositFactoryImpl();
        Deposit deposit = new Deposit(depositFactory);
        deposit.setBalance(0.0);
        deposit.setOwnMoney(0.0);
        deposit.setRate(rate);
        deposit.setUser(user);
        deposit.setCloseDate(LocalDateTime.now().plusMonths(rate.getNumberOfMonth()));
        depositRepository.save(deposit);
    }

    @Transactional
    public void closeDeposit(User user, long depositId, long debitCardId) {
        DebitCard card = getCard(user, debitCardId);
        Deposit deposit = getDeposit(user, depositId);

        if (!deposit.getRate().isHasEarlyClosed()) {
            throw new RuntimeException("Отказано. Депозит невозможно закрыть досрочно");
        }

        card.setBalance(card.getBalance() + deposit.getBalance());
        deposit.setBalance(0.0);
        deposit.setCloseDate(LocalDateTime.now());
        debitCardRepository.save(card);
        depositRepository.save(deposit);
    }

    @Transactional
    public void decreaseDeposit(User user, long depositId, long debitCardId, double amount) {
        DebitCard card = getCard(user, debitCardId);
        Deposit deposit = getDeposit(user, depositId);

        validateDecrease(deposit, amount);

        card.setBalance(card.getBalance() + amount);
        deposit.setBalance(deposit.getBalance() - amount);

        debitCardRepository.save(card);
        depositRepository.save(deposit);
    }

    private void validateDecrease(Deposit deposit, double amount) {
        if (!deposit.getRate().isHasWithdrawal()) {
            throw new RuntimeException("Отказано. Депозит не предусматривает снятие средств");
        }

        if ( amount >= deposit.getBalance() - deposit.getRate().getMinAmount()) {
            throw new RuntimeException("Отказано. Снимаемая сумма меньше минимального значания вклада");
        }

    }

    private DebitCard getCard(User user, long debitCardId) {
        return debitCardRepository.findByUser(user)
                .orElseThrow(CardNotFoundException::new)
                .stream()
                .filter(c -> c.getId() == debitCardId)
                .findAny()
                .orElseThrow(CardNotFoundException::new);
    }

    private Deposit getDeposit(User user, long depositId) {
        return depositRepository.findByUser(user)
                .orElseThrow(DepositNotFoundException::new)
                .stream()
                .filter(d -> d.getId() == depositId)
                .findAny()
                .orElseThrow(DepositNotFoundException::new);
    }

    @Scheduled(fixedRate = 86400000)
    public void calculateCapitalizePayment() {
        log.info("IN calculateCapitalizePayment() - start");
        List<Deposit> deposits = depositRepository.findAll()
                .stream()
                .filter(d -> d.getRate().isHasCapitalized())
                .collect(Collectors.toList());

        for (Deposit deposit : deposits) {
            deposit.setBalance(deposit.getBalance() + (deposit.getBalance() * deposit.getRate().getPercent()));
            depositRepository.save(deposit);
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void calculatePayment() {
        log.info("IN calculatePayment() - start");
        List<Deposit> deposits = depositRepository.findAll()
                .stream()
                .filter(d -> !d.getRate().isHasCapitalized())
                .collect(Collectors.toList());

        for (Deposit deposit : deposits) {
            deposit.setBalance(deposit.getBalance() + (deposit.getRate().getMinAmount() * deposit.getRate().getPercent()));
            depositRepository.save(deposit);
        }
    }

    @Scheduled(fixedRate = 86400000)
    public void extendDeposit() {
        log.info("IN extendDeposit() - start");
        List<Deposit> deposits = depositRepository.findAll()
                .stream()
                .filter(d -> LocalDateTime.now().isAfter(d.getCloseDate()))
                .filter(d -> d.getRate().isHasEarlyClosed())
                .collect(Collectors.toList());

        for (Deposit deposit : deposits) {
            deposit.setCloseDate(deposit.getCloseDate().plusMonths(1));
            depositRepository.save(deposit);
        }
    }

    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void closeDeposit() {
        log.info("IN closeDeposit() - start");
        List<Deposit> deposits = depositRepository.findAll()
                .stream()
                .filter(d -> LocalDateTime.now().isAfter(d.getCloseDate()))
                .filter(d -> !d.getRate().isHasEarlyClosed())
                .collect(Collectors.toList());

        for (Deposit deposit : deposits) {
            User user = deposit.getUser();
            DebitCard debitCard = debitCardRepository.findByUser(user)
                    .orElseThrow(UserNotFoundException::new)
                    .stream()
                    .findFirst().orElseThrow(CardNotFoundException::new);
            closeDeposit(user, deposit.getId(), debitCard.getId());
        }
    }


}
