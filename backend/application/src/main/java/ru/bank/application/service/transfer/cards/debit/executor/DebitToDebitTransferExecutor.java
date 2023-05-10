package ru.bank.application.service.transfer.cards.debit.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.exception.transfer.RecipientAccountNotFoundException;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.factory.transfer.type.TransferType;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DebitToDebitTransferExecutor implements DebitTransferExecutor {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    DebitCardRepository debitCardRepository;
    @Autowired
    DebitToCreditTransferExecutor creditTransferExecutor;

    @Override
    public void executeTransfer(Transfer transfer, DebitCard cardSender){
        String cardNumber = transfer.getRecipientAccount();
        double amount = transfer.getAmount();
        double commission = transfer.getCommission();

        DebitCard cardRecipient = debitCardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow(RecipientAccountNotFoundException::new);
        cardSender.setBalance(cardSender.getBalance() - (amount + (amount * commission)));
        cardRecipient.setBalance(cardRecipient.getBalance() + amount);
        transfer.setStatus(TransferStatus.DONE);
        transfer.setDate(LocalDateTime.now());
        debitCardRepository.save(cardSender);
        debitCardRepository.save(cardRecipient);
        transferRepository.save(transfer);
    }

    @Override
    public void defineExecutor(Transfer transfer, DebitCard cardSender) {
        if (Objects.equals(transfer.getType(), TransferType.INTERNAL_TO_DEBIT_CARD)){
            executeTransfer(transfer, cardSender);
        } else {
            creditTransferExecutor.defineExecutor(transfer, cardSender);
        }
    }


}
