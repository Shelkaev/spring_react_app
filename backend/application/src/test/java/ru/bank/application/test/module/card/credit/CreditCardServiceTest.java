package ru.bank.application.test.module.card.credit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bank.application.Application;
import ru.bank.application.api.data.cards.CreditCardСonditionsApiRequest;
import ru.bank.application.api.data.cards.PenniApiRequest;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;
import ru.bank.persistence.factory.cards.credit.CreditCardFactoryStandard;
import ru.bank.persistence.repository.card.CreditCardRepository;

import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class CreditCardServiceTest {

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private UserService userService;

    @Autowired
    private CreditCardRepository creditCardRepository;


    @Test
    public void createCreditCardTest() {
        User user = userService.loadUserByLogin("test");
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        CreditCard card = creditCardService.createCreditCard(user, cardFactory);
        Assert.assertEquals(0, card.getBalance(), 0);
        Assert.assertTrue(card.getCardNumber().startsWith("11110"));
        Assert.assertTrue(card.getCloseDate().isAfter(LocalDateTime.now()));
        Assert.assertEquals("test", card.getUser().getLogin());
        Assert.assertEquals(0, card.getLimit(), 0);
        Assert.assertEquals(0, card.getCreditPercent(), 0);
    }

    @Test
    public void find_CreditCardsByUserTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findByUser(user);
        for (CreditCard card : cards) {
            Assert.assertNotSame(card.getState(), CardState.REJECTED);
            Assert.assertNotSame(card.getState(), CardState.NOT_ACTIVE);
        }
    }

    @Test
    public void findActive_CreditCardsByUserTest() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        for (CreditCard card : cards) {
            Assert.assertEquals(CardState.ACTIVE, card.getState());
        }
    }

    @Test(expected = CardNotFoundException.class)
    public void getUserCardByIdTest() {
        User otherUser = userService.loadUserByLogin("test_1");
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        CreditCard otherCard = creditCardService.createCreditCard(otherUser, cardFactory);
        User user = userService.loadUserByLogin("test");
        creditCardService.getUserCardById(user, otherCard.getId());
    }

    @Test(expected = RuntimeException.class)
    public void setBedCurrencyPercentTest() {
        CreditCardСonditionsApiRequest request = generateRequest(50000, 0.3, 0.5, 0.01);
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        creditCardService.setParameters(card, request);
    }

    @Test(expected = RuntimeException.class)
    public void setBedCreditPercentTest() {
        CreditCardСonditionsApiRequest request = generateRequest(50000, 0.1, 0.6, 0.01);
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        creditCardService.setParameters(card, request);
    }

    @Test(expected = RuntimeException.class)
    public void setBedPenniPercentTest() {
        CreditCardСonditionsApiRequest request = generateRequest(50000, 0.1, 0.4, 0.1);
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        creditCardService.setParameters(card, request);
    }

    @Test
    public void setParametersToCardTest() {
        CreditCardСonditionsApiRequest request = generateRequest(50000, 0.1, 0.2, 0.01);
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        creditCardService.setParameters(card, request);
        Assert.assertEquals(0.1, card.getCurrencyPercent(), 0);
        Assert.assertEquals(0.2, card.getCreditPercent(), 0);
        Assert.assertEquals(0.01, card.getPenniPercent(), 0);
        Assert.assertEquals(CardState.PROPOSED, card.getState());
    }

    @Test(expected = RuntimeException.class)
    public void setPenni_BedPenniPercentTest() {
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        PenniApiRequest request = new PenniApiRequest();
        request.setPenniPercent(0.02);
        request.setCurrencyPercent(0.2);
        creditCardService.setPenni(card, request);
    }

    @Test(expected = RuntimeException.class)
    public void setPenni_BedCurrencyPercentTest() {
        CreditCard card = creditCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        PenniApiRequest request = new PenniApiRequest();
        request.setPenniPercent(0.009);
        request.setCurrencyPercent(0.3);
        creditCardService.setPenni(card, request);
    }

    @Test
    public void setPenniTest() {
        CreditCard card = createCreditCard();
        creditCardService.changeStateCreditCard(card, "ACTIVE");
        creditCardService.changePenniNote(card);
        PenniApiRequest request = new PenniApiRequest();
        request.setPenniPercent(0.009);
        request.setCurrencyPercent(0.05);
        creditCardService.setPenni(card, request);
        Assert.assertEquals(0.05, card.getCurrencyPercent(), 0);
        Assert.assertEquals(0.009, card.getPenniPercent(), 0);
    }

    @Test
    public void changeStateCreditCardTest(){
        CreditCard card = createCreditCard();
        creditCardService.changeStateCreditCard(card, "ACTIVE");
        Assert.assertEquals(CardState.ACTIVE, card.getState());
    }

    private CreditCard createCreditCard(){
        User user = userService.loadUserByLogin("test");
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        CreditCard card = creditCardService.createCreditCard(user, cardFactory);
        CreditCardСonditionsApiRequest request = generateRequest(50000, 0.1, 0.2, 0.01);
        creditCardService.setParameters(card, request);
        return card;
    }

    private CreditCardСonditionsApiRequest generateRequest(double limit,
                                                           double currencyPercent,
                                                           double creditPercent,
                                                           double penniPercent) {
        CreditCardСonditionsApiRequest request = new CreditCardСonditionsApiRequest();
        request.setLimit(limit);
        request.setCurrencyPercent(currencyPercent);
        request.setCreditPercent(creditPercent);
        request.setPenniPercent(penniPercent);
        return request;
    }

}

