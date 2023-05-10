package ru.bank.persistence.repository.transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;


import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<List<Transfer>> findAllByDebitCard(DebitCard debitCard);
    Optional<Transfer> findById(Long id);

    @Query(value = "SELECT * FROM transfer WHERE debit_card_id = ?1 " +
            "AND status NOT LIKE 'DRAFT' ORDER BY date DESC LIMIT 10",
            nativeQuery = true)
    Optional<List<Transfer>> findLast10ByDebitCardIdNative(Long id);

    @Query(value = "SELECT * FROM transfer WHERE status LIKE 'BLOCK' ORDER BY date DESC", nativeQuery = true)
    Optional<List<Transfer>> findBlockedTransfersNative();

    @Query(value = "SELECT * FROM transfer WHERE status LIKE 'DRAFT' ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Transfer> findLastDraftTransferNative();

}
