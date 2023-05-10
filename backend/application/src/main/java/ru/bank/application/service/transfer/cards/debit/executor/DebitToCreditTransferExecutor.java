package ru.bank.application.service.transfer.cards.debit.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.exception.card.CardNotApprovedException;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.exception.credit.CreditNotFoundException;
import ru.bank.application.exception.transfer.RecipientAccountNotFoundException;
import ru.bank.application.service.credit.CreditService;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.credit.Credit;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.factory.transfer.type.TransferType;
import ru.bank.persistence.repository.card.CreditCardRepository;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.credit.CreditRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class DebitToCreditTransferExecutor implements DebitTransferExecutor {

    @Autowired
    TransferRepository transferRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    DebitCardRepository debitCardRepository;
    @Autowired
    DebitToDepositTransferExecutor depositTransferExecutor;
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    CreditService creditService;


    @Override
    public void executeTransfer(Transfer transfer, DebitCard cardSender) {
        String cardNumber = transfer.getRecipientAccount();
        double amount = transfer.getAmount();
        double commission = transfer.getCommission();
        CreditCard cardRecipient = creditCardRepository.findAll()
                .stream()
                .filter(c -> c.getState() == CardState.ACTIVE)
                .filter(c -> c.getCardNumber().equals(cardNumber))
                .findAny().orElseThrow(CardNotFoundException::new);

        cardSender.setBalance(cardSender.getBalance() - (amount + (amount * commission)));
        cardRecipient.setBalance(cardRecipient.getBalance() + amount);
        try {
            payToCredit(cardRecipient, amount);
        } catch (CreditNotFoundException ignore) {}
        transfer.setStatus(TransferStatus.DONE);
        transfer.setDate(LocalDateTime.now());
        debitCardRepository.save(cardSender);
        creditCardRepository.save(cardRecipient);
        transferRepository.save(transfer);
    }

    @Override
    public void defineExecutor(Transfer transfer, DebitCard cardSender) {
        if (Objects.equals(transfer.getType(), TransferType.INTERNAL_TO_CREDIT_CARD)) {
            executeTransfer(transfer, cardSender);
        } else {
            depositTransferExecutor.defineExecutor(transfer, cardSender);
        }
    }

    private void payToCredit(CreditCard card, double amount) {
        List<Credit> credits = creditService.getActiveCredits(card);
        double summa = amount;
        for (Credit credit : credits) {
            if (summa == 0) {
                break;
            }
            if (summa > credit.getDuty()) {
                summa = summa - credit.getDuty();
                card.setTotalDuty(card.getTotalDuty() - credit.getDuty());
                credit.setDuty(0);
                creditRepository.save(credit);
            }
            if (summa < credit.getDuty()) {
                credit.setDuty(credit.getDuty() - summa);
                card.setTotalDuty(card.getTotalDuty() - summa);
                summa = 0;
                creditRepository.save(credit);
            }
            if (summa == credit.getDuty()) {
                card.setTotalDuty(card.getTotalDuty() - credit.getDuty());
                credit.setDuty(0);
                summa = 0;
                creditRepository.save(credit);
            }
        }
    }
}
