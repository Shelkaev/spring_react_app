package ru.bank.application.dto.deposit;

import ru.bank.persistence.entity.deposit.Deposit;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DepositFactoryDto {

    public DepositFactoryDto() {

    }
    private long rateId;
    private String rateName;
    private double maxAmount;
    private double minAmount;
    private double percent;
    private boolean hasEarlyClosed;
    private  boolean hasIncreased;
    private boolean hasCapitalized;
    private boolean hasWithdrawal;
    private long depositId;
    private double balance;
    private String closeDate;

    public static DepositDto crateDeposit(Deposit deposit) {
        return new DepositDto(
                deposit.getRate().getId(),
                deposit.getRate().getName(),
                deposit.getRate().getMaxAmount(),
                deposit.getRate().getMinAmount(),
                deposit.getRate().getPercent(),
                deposit.getRate().isHasEarlyClosed(),
                deposit.getRate().isHasIncreased(),
                deposit.getRate().isHasCapitalized(),
                deposit.getRate().isHasWithdrawal(),
                deposit.getDepositNumber(),
                deposit.getId(),
                deposit.getBalance(),
                deposit.getCloseDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public static List<DepositDto> createDebitCardList(List<Deposit> deposits) {
        List<DepositDto> dtoDeposits = new ArrayList<>();
        for (Deposit deposit : deposits) {
            dtoDeposits.add(crateDeposit(deposit));
        }
        return dtoDeposits;
    }

}
