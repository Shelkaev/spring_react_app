package ru.bank.application.service.cards.credit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bank.application.api.data.cards.CreditCardСonditionsApiRequest;
import ru.bank.application.api.data.cards.PenniApiRequest;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.service.cards.credit.state.ActiveCreditCardStateChanger;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;
import ru.bank.persistence.repository.card.CreditCardRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CreditCardService {
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    ActiveCreditCardStateChanger cardStateChanger;

    public CreditCard createCreditCard(User user, CreditCardFactory creditCardFactory) {
        CreditCard creditCard = new CreditCard(creditCardFactory);
        creditCard.setUser(user);
        creditCardRepository.save(creditCard);
        return creditCard;
    }

    public List<CreditCard> findByUser(User user) {
        return creditCardRepository
                .findByUser(user)
                .orElseThrow(CardNotFoundException::new)
                .stream()
                .filter(c -> c.getState() != CardState.REJECTED)
                .filter(c -> c.getState() != CardState.NOT_ACTIVE)
                .collect(Collectors.toList());
    }

    public List<CreditCard> findActiveCardsByUser(User user) {
        return creditCardRepository
                .findByUser(user)
                .orElseThrow(CardNotFoundException::new)
                .stream()
                .filter(c -> c.getState() == CardState.ACTIVE)
                .collect(Collectors.toList());
    }

    public CreditCard findById(Long id) {
        return creditCardRepository.findById(id).orElseThrow(CardNotFoundException::new);
    }

    public CreditCard getUserCardById(User user, Long cardId) {
        return findByUser(user)
                .stream()
                .filter(c -> c.getId() == cardId)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
    }

    public void setParameters(CreditCard card, CreditCardСonditionsApiRequest request) {
        validateСonditionsRequest(request);
        card.setBalance(request.getLimit());
        card.setLimit(request.getLimit());
        card.setCurrencyPercent(request.getCurrencyPercent());
        card.setCreditPercent(request.getCreditPercent());
        card.setPenniPercent(request.getPenniPercent());
        card.setState(CardState.PROPOSED);
        creditCardRepository.save(card);
    }

    public void setPenni(CreditCard card, PenniApiRequest request) {
        if (request.getPenniPercent() >= card.getPenniPercent()) {
            throw new RuntimeException("Операция не выполнена. Новый процент по пенни не может быть выше текущего");
        }
        if (request.getCurrencyPercent() >= card.getCurrencyPercent()) {
            throw new RuntimeException("Операция не выполнена. Новая валютная комиссия не может быть выше текущей");
        }
        card.setPenniPercent(request.getPenniPercent());
        card.setCurrencyPercent(request.getCurrencyPercent());
        card.setNote("Параметры карты изменены "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        creditCardRepository.save(card);
    }

    public void changePenniNote(CreditCard card) {
        card.setNote("changePenni");
        creditCardRepository.save(card);
    }

    public List<CreditCard> getOrderedCreditCards() {
        return creditCardRepository.findOrderedCardsNative().orElseThrow(CardNotFoundException::new);
    }

    public List<CreditCard> getChangePenniCards() {
        return creditCardRepository.findChangePenniCardsNative().orElseThrow(CardNotFoundException::new);
    }

    public void changeStateCreditCard(CreditCard card, String state) {
        cardStateChanger.defineState(card, state);
    }

    @Scheduled(fixedRate = 86400000)
    public void closeCreditCards() {
        log.info("IN closeCreditCards() - start");
        List<CreditCard> cards = creditCardRepository.findAll()
               .stream()
               .filter(card -> card.getCloseDate().isBefore(LocalDateTime.now()))
               .collect(Collectors.toList());

        for (CreditCard card : cards ) {
            if (card.getTotalDuty() == 0) {
                card.setState(CardState.NOT_ACTIVE);
                creditCardRepository.save(card);
            }
        }

    }

    private void validateСonditionsRequest(CreditCardСonditionsApiRequest request) {
        if (request.getPenniPercent() > 0.01) {
            throw new RuntimeException("Операция не выполнена. Процент по пенни не может быть выше 0.01");
        }
        if (request.getCreditPercent() >= 0.5) {
            throw new RuntimeException("Операция не выполнена. Процентная ставка не может быть выше 0.5");
        }
        if (request.getCurrencyPercent() > 0.1) {
            throw new RuntimeException("Операция не выполнена. Валютная коммиссия не может быть выше 0.1");
        }
    }

    public CreditCard findCardById(List<CreditCard> cards, long cardId) {
        return cards.stream()
                .filter(c -> c.getId() == cardId)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
    }


}
