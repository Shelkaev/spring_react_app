package ru.bank.application.dto.transfer;

import ru.bank.persistence.entity.transfer.Transfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BlockedTransferDtoFactory {

    public static BlockedTransferDto createDto(Transfer transfer) {
        return new BlockedTransferDto(
                transfer.getId(),
                transfer.getRecipientAccount(),
                transfer.getDebitCard().getCardNumber(),
                setDateOrEmpty(transfer.getDate()),
                transfer.getDebitCard().getUser().getLogin(),
                transfer.getAmount()
        );
    }

    public static List<BlockedTransferDto> createTransferDtoList(List<Transfer> transfers) {
        List<BlockedTransferDto> dtoTransfers = new ArrayList<>();
        for(Transfer transfer : transfers) {
            dtoTransfers.add(createDto(transfer));
        }
        return dtoTransfers;
    }

    public static String setDateOrEmpty(LocalDateTime date){
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }
}

