package ru.bank.application.service.transfer.cards.debit.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.factory.transfer.type.TransferType;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ExternalDebitTransferExecutor implements DebitTransferExecutor{
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    DebitCardRepository debitCardRepository;

    @Override
    public void executeTransfer(Transfer transfer, DebitCard cardSender){
        double amount = transfer.getAmount();
        double commission = transfer.getCommission();
        cardSender.setBalance(cardSender.getBalance() - (amount + (amount * commission)));
        transfer.setStatus(TransferStatus.DONE);
        transfer.setDate(LocalDateTime.now());
        debitCardRepository.save(cardSender);
        transferRepository.save(transfer);
    }
    @Override
    public void defineExecutor(Transfer transfer, DebitCard cardSender) {
        if (Objects.equals(transfer.getType(), TransferType.EXTERNAL)){
            executeTransfer(transfer, cardSender);
        } else {
            throw new RuntimeException("Возникла непредвиденная ошибка. Перевод не выполнен");
        }
    }
}
