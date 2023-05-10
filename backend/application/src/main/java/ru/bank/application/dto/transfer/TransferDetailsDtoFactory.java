package ru.bank.application.dto.transfer;

import ru.bank.application.dto.card.DebitCardDto;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferDetailsDtoFactory {

    public static TransferDetailsDto createTransferWithDebitCard(Transfer transfer) {
        return new TransferDetailsDto(
                transfer.getId(),
                transfer.getDebitCard().getId(),
                transfer.getType().name(),
                setDateOrEmpty(transfer.getDate()),
                transfer.getStatus().name(),
                transfer.getCommission(),
                transfer.getAmount(),
                muteCardNumber(transfer.getDebitCard().getCardNumber()),
                transfer.getRecipientAccount()
        );
    }

    public static List<TransferDetailsDto> createTransferDtoList(List<Transfer> transfers) {
        List<TransferDetailsDto> dtoTransfers = new ArrayList<>();
        for(Transfer transfer : transfers) {
            dtoTransfers.add(createTransferWithDebitCard(transfer));
        }
        return dtoTransfers;
    }

    private static String muteCardNumber(String cardNumber){
        Matcher matcher = Pattern.compile("\\d{4}$").matcher(cardNumber);
        matcher.find();
        return "**** **** **** " + matcher.group();
    }

    public static String setDateOrEmpty(LocalDateTime date){
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

}
