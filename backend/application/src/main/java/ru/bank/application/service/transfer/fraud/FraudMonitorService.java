package ru.bank.application.service.transfer.fraud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bank.application.exception.transfer.FraudMonitorException;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.factory.transfer.type.TransferType;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
public class FraudMonitorService {
    @Autowired
    TransferRepository transferRepository;

    public void validate(Transfer transfer){
        if (transfer.getStatus() != TransferStatus.DRAFT) {
            blockTransfer(transfer);
            throw new RuntimeException("Перевод уже был выполнен");
        }
        if (transfer.getAmount() > 50000 && transfer.getType() != TransferType.INTERNAL_TO_DEPOSIT) {
            blockTransfer(transfer);
            log.info("IN FraudMonitorService.validate() - Transfer : {} is blocked", transfer);
            throw new FraudMonitorException();
        }
    }

    private void blockTransfer(Transfer transfer){
        transfer.setStatus(TransferStatus.BLOCK);
        transfer.setDate(LocalDateTime.now());
        transferRepository.save(transfer);
    }

}
