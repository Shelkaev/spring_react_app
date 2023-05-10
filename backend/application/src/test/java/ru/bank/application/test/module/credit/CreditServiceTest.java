package ru.bank.application.test.module.credit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bank.application.Application;
import ru.bank.application.api.data.credit.CreditApiRequest;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.credit.CreditService;
import ru.bank.application.service.credit.currency.CurrencyValue;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.credit.Credit;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.repository.credit.CreditRepository;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class CreditServiceTest {
    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private CreditService creditService;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditCardService creditCardService;


    @Test
    public void getCreditDetailsTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(200);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("RU");
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        Assert.assertEquals(0, creditDetails.getCurrencyCommission(), 0);
        Assert.assertEquals(cards.get(0).getCreditPercent(), creditDetails.getCreditPercent(), 0);
        Assert.assertEquals(200, creditDetails.getAmount(), 0);
        Assert.assertEquals("RU", creditDetails.getCurrency());
    }


    @Test
    public void getCreditRUTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(200);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("RU");
        double totalDuty = cards.get(0).getTotalDuty();
        double balance = cards.get(0).getBalance();
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        creditService.getCredit(user, creditDetails);
        CreditCard card = creditCardService.getUserCardById(user, cards.get(0).getId());
        Assert.assertEquals(card.getTotalDuty() - totalDuty, 200 + (200 * card.getCreditPercent()), 0);
        Assert.assertEquals(balance - card.getBalance(), 200 + (200 * card.getCreditPercent()), 0);
    }

    @Test
    public void getCreditUSDTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(2);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("USD");
        double totalDuty = cards.get(0).getTotalDuty();
        double balance = cards.get(0).getBalance();
        double totalAmountRU = 2 * CurrencyValue.getUsd() +
                (2 * CurrencyValue.getUsd() * cards.get(0).getCurrencyPercent()) +
                (2 * CurrencyValue.getUsd() * cards.get(0).getCreditPercent());
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        creditService.getCredit(user, creditDetails);
        CreditCard card = creditCardService.getUserCardById(user, cards.get(0).getId());
        Assert.assertEquals(card.getTotalDuty() - totalDuty, totalAmountRU, 0.01);
        Assert.assertEquals(balance - card.getBalance(), totalAmountRU, 0.01);
    }

    @Test
    public void getCreditEURTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(2);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("EUR");
        double totalDuty = cards.get(0).getTotalDuty();
        double balance = cards.get(0).getBalance();
        double totalAmountRU = 2 * CurrencyValue.getEur() +
                (2 * CurrencyValue.getEur() * cards.get(0).getCurrencyPercent()) +
                (2 * CurrencyValue.getEur() * cards.get(0).getCreditPercent());
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        creditService.getCredit(user, creditDetails);
        CreditCard card = creditCardService.getUserCardById(user, cards.get(0).getId());
        Assert.assertEquals(card.getTotalDuty() - totalDuty, totalAmountRU, 0.01);
        Assert.assertEquals(balance - card.getBalance(), totalAmountRU, 0.01);
    }

    @Test
    public void getActiveCreditsTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(10);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("RU");
        double totalDuty = cards.get(0).getTotalDuty();
        double balance = cards.get(0).getBalance();
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        creditService.getCredit(user, creditDetails);
        List<Credit> credits = creditService.getActiveCredits(cards.get(0));
        for (CreditCard card : cards) {
            Assert.assertEquals(CardState.ACTIVE, card.getState());
        }
    }

}
