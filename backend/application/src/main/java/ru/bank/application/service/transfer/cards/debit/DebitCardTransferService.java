package ru.bank.application.service.transfer.cards.debit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.application.api.data.transfer.TransferApiRequest;
import ru.bank.application.api.data.transfer.TransferExecuteApiRequest;
import ru.bank.application.dto.transfer.TransferDetailsDto;
import ru.bank.application.dto.transfer.TransferDetailsDtoFactory;
import ru.bank.application.exception.transfer.FraudMonitorException;
import ru.bank.application.exception.transfer.TransferNotFoundException;
import ru.bank.application.service.cards.debit.DebitCardService;
import ru.bank.application.service.transfer.cards.debit.executor.DebitToDebitTransferExecutor;
import ru.bank.application.service.transfer.fraud.FraudMonitorService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.transfer.commision.CommissionFactory;
import ru.bank.persistence.factory.transfer.type.TypeFactory;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.util.List;

@Service
@Slf4j
public class DebitCardTransferService {
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    FraudMonitorService fraudMonitorService;
    @Autowired
    DebitCardService debitCardService;
    @Autowired
    DebitToDebitTransferExecutor debitToDebitExecutor;


    public List<Transfer> findByDebitCard(DebitCard card) {
        return transferRepository.findAllByDebitCard(card).orElseThrow(TransferNotFoundException::new);
    }


    public TransferDetailsDto getTransferDetails(User user, TransferApiRequest request) {
        DebitCard card = debitCardService.getUserCardById(user, request.getCardId());
        Transfer transfer = new Transfer();
        transfer.setDebitCard(card);
        transfer.setRecipientAccount(request.getBeneficiaryAccount());
        transfer.setAmount(request.getAmount());
        transfer.setStatus(TransferStatus.DRAFT);
        transfer.setType(TypeFactory.getType(request.getBeneficiaryAccount()));
        transfer.setCommission(CommissionFactory.calculateTransferCommission(transfer.getType()));
        transferRepository.save(transfer);
        return TransferDetailsDtoFactory.createTransferWithDebitCard(transfer);
    }


    @Transactional(noRollbackFor = FraudMonitorException.class)
    public void executeTransfer(User user, TransferExecuteApiRequest transferDetails) {
        DebitCard card = debitCardService.getUserCardById(user, transferDetails.getDebitCardId());
        Transfer transfer = getCardTransferById(card, transferDetails.getTransferId());
        validateAmount(card, transfer.getAmount(), transfer.getCommission());
        validateAccountNumber(transfer);
        fraudMonitorService.validate(transfer);
        debitToDebitExecutor.defineExecutor(transfer, card);
    }


    public List<Transfer> getOutputHistory(long cardId) {
        return transferRepository
                .findLast10ByDebitCardIdNative(cardId)
                .orElseThrow(TransferNotFoundException::new);
    }


    public List<Transfer> getBlockedTransfers() {
        return transferRepository
                .findBlockedTransfersNative()
                .orElseThrow(TransferNotFoundException::new);
    }

    @Transactional
    public void executeTransferAdmin(long transferId) {
        Transfer transfer = transferRepository
                .findById(transferId)
                .orElseThrow(TransferNotFoundException::new);
        DebitCard card = debitCardService.findById(transfer.getDebitCard().getId());
        try {
            validateAmount(card, transfer.getAmount(), transfer.getCommission());
            debitToDebitExecutor.defineExecutor(transfer, card);
        } catch (RuntimeException e) {
            transfer.setStatus(TransferStatus.DRAFT);
            transferRepository.save(transfer);
        }

    }


    public void rejectTransferAdmin(long transferId) {
        Transfer transfer = transferRepository
                .findById(transferId)
                .orElseThrow(TransferNotFoundException::new);
        transfer.setStatus(TransferStatus.REJECTED);
        transferRepository.save(transfer);
    }


    private void validateAmount(DebitCard card, double amount, double commission) {
        if (card.getBalance() < amount + (amount * commission)) throw new RuntimeException("Недостаточно средств");
    }

    private void validateAccountNumber(Transfer transfer) {
        if(transfer.getRecipientAccount().equals(transfer.getDebitCard().getCardNumber())) {
            throw new RuntimeException("Перевод отклонен. Причина : попытка перевода на счет отправителя");
        }
    }

    public Transfer getCardTransferById(DebitCard card, Long transferId) {
        return findByDebitCard(card)
                .stream()
                .filter(c -> c.getId() == transferId)
                .findFirst()
                .orElseThrow(TransferNotFoundException::new);
    }

}
