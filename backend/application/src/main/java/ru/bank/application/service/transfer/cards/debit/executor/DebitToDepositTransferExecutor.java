package ru.bank.application.service.transfer.cards.debit.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.exception.deposit.DepositNotFoundException;
import ru.bank.persistence.entity.deposit.Deposit;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.factory.transfer.type.TransferType;
import ru.bank.persistence.repository.deposit.DepositRepository;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DebitToDepositTransferExecutor implements DebitTransferExecutor {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    DebitCardRepository debitCardRepository;
    @Autowired
    DepositRepository depositRepository;
    @Autowired
    ExternalDebitTransferExecutor externalTransferExecutor;

    @Override
    public void executeTransfer(Transfer transfer, DebitCard cardSender) {
        String cardNumber = transfer.getRecipientAccount();
        double amount = transfer.getAmount();
        double commission = transfer.getCommission();

        Deposit deposit = depositRepository
                .findByDepositNumber(transfer.getRecipientAccount())
                .orElseThrow(DepositNotFoundException::new);

        validateIncrease(deposit, transfer.getAmount());

        cardSender.setBalance(cardSender.getBalance() - (amount + (amount * commission)));
        deposit.setBalance(deposit.getBalance() + amount);
        transfer.setStatus(TransferStatus.DONE);
        transfer.setDate(LocalDateTime.now());
        debitCardRepository.save(cardSender);
        depositRepository.save(deposit);
        transferRepository.save(transfer);
    }

    @Override
    public void defineExecutor(Transfer transfer, DebitCard cardSender) {
        if (Objects.equals(transfer.getType(), TransferType.INTERNAL_TO_DEPOSIT)) {
            executeTransfer(transfer, cardSender);
        } else {
            externalTransferExecutor.defineExecutor(transfer, cardSender);
        }
    }

    private void validateIncrease(Deposit deposit, double amount) {
        if (!deposit.getRate().isHasIncreased() && deposit.getBalance() != 0.0) {
            throw new RuntimeException("Отказано. Депозит не пополняем");
        }
        if (deposit.getBalance() + amount > deposit.getRate().getMaxAmount()) {
            throw new RuntimeException("Отказано. Баланс депозита не должен превышать: "
                    + deposit.getRate().getMaxAmount());
        }
        if (deposit.getBalance() == 0.0 && deposit.getRate().getMinAmount() >  amount) {
            throw new RuntimeException("Отказано. Минимальная сумма для пополнения: "
                    + deposit.getRate().getMinAmount());
        }
    }

}
