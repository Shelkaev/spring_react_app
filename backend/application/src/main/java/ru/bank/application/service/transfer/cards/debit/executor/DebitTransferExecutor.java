package ru.bank.application.service.transfer.cards.debit.executor;

import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;

public interface DebitTransferExecutor {
    void executeTransfer(Transfer transfer, DebitCard cardSender);
    void defineExecutor(Transfer transfer, DebitCard cardSender);
}
