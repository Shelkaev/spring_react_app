package ru.bank.application.service.credit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.application.api.data.credit.CreditApiRequest;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.exception.credit.CreditNotFoundException;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.credit.date.repayment.DateRepayment;
import ru.bank.application.service.credit.duty.RuDutySetter;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.repository.card.CreditCardRepository;
import ru.bank.persistence.repository.credit.CreditRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class CreditService {
    @Autowired
    CreditCardService creditCardService;
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    RuDutySetter dutySetter;


    public CreditDetailsDto getCreditDetails(User user, CreditApiRequest request) {
        CreditCard card = creditCardService.getUserCardById(user, request.getCardId());
        CreditDetailsDto creditDetails = new CreditDetailsDto();
        creditDetails.setCardId(card.getId());
        creditDetails.setCurrency(request.getCurrency());
        creditDetails.setCreditPercent(card.getCreditPercent());
        creditDetails.setCurrencyCommission(defineCommission(request.getCurrency(), card));
        creditDetails.setAmount(request.getAmount());
        creditDetails.setDateRepayment(DateRepayment.define(card.getCloseDate())
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        return creditDetails;
    }

    @Transactional
    public void getCredit(User user, CreditDetailsDto request) {
        CreditCard card = creditCardService.getUserCardById(user, request.getCardId());
        checkDuty(card);
        Credit credit = new Credit();
        credit.setCreditCard(card);
        credit.setDateRepayment(DateRepayment.define(card.getCloseDate()));
        dutySetter.defineCurrency(request, credit);
        card.setBalance(card.getBalance() - credit.getDuty());
        card.setTotalDuty(card.getTotalDuty() + credit.getDuty());
        creditCardRepository.save(card);
        creditRepository.save(credit);
    }

    public List<Credit> getActiveCredits(CreditCard card) {
        return creditRepository
                .findAllByCreditCard(card)
                .orElseThrow(CreditNotFoundException::new)
                .stream()
                .filter(credit -> credit.getDuty() != 0)
                .collect(Collectors.toList());
    }


    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void chargePenni() {
        log.info("IN chargePenni() - start");
        List<Credit> credits = creditRepository.findAllActiveNative().orElse(new ArrayList<>())
                .stream()
                .filter(c -> LocalDateTime.now().isAfter(c.getDateRepayment()))
                .filter(this::hasPenni)
                .collect(Collectors.toList());

        for (Credit credit : credits) {
            double penni = credit.getCreditCard().getPenniPercent() * credit.getDuty();
            CreditCard card = credit.getCreditCard();
            credit.setDuty(credit.getDuty() + penni);
            card.setTotalDuty(card.getTotalDuty() + penni);
            card.setBalance(card.getBalance() - penni);
            if (card.getNote() == null) {
                card.setNote("hasPenni");
            }
            creditCardRepository.save(card);
            creditRepository.save(credit);
            }
    }

    private double defineCommission(String currency, CreditCard card) {
        if (currency.equals("RU")) {
            return 0;
        } else {
            return card.getCurrencyPercent();
        }
    }


    private void checkDuty(CreditCard card) {
        Credit credit = null;
        try {
            credit = getActiveCredits(card)
                    .stream()
                    .filter(c -> LocalDateTime.now().isAfter(c.getDateRepayment()))
                    .findAny()
                    .orElse(null);

        } catch (CreditNotFoundException ignore) {
        }
        if (credit != null) throw new RuntimeException("Отказано. Существуют просроченные кредиты");
    }

    private boolean hasPenni (Credit credit) {
        return (credit.getAmountRu() * 1.4) > credit.getDuty();
    }


}
