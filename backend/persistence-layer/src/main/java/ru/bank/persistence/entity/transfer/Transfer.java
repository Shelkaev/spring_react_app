package ru.bank.persistence.entity.transfer;

import ru.bank.persistence.entity.deposit.Deposit;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.factory.transfer.type.TransferType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private double amount;
    private double commission;
    @Column(nullable = false, length = 20)
    private String recipientAccount;
    private LocalDateTime date;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferStatus status;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferType type;

    @ManyToOne
    @JoinColumn(name="account_id")
    private Deposit account;

    @ManyToOne
    @JoinColumn(name="debit_card_id")
    private DebitCard debitCard;


    public Transfer() {
    }

    public Transfer(double amount, double commission,
                    String recipientAccount,
                    TransferStatus status,
                    TransferType type,
                    DebitCard debitCard) {
        this.amount = amount;
        this.commission = commission;
        this.recipientAccount = recipientAccount;
        this.status = status;
        this.type = type;
        this.debitCard = debitCard;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(String recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Deposit getAccount() {
        return account;
    }

    public void setAccount(Deposit account) {
        this.account = account;
    }

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", amount=" + amount +
                ", commission=" + commission +
                ", recipientAccount='" + recipientAccount + '\'' +
                ", date=" + date +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
