package ru.bank.application.test.module.card.debit;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bank.application.Application;
import ru.bank.application.service.cards.debit.DebitCardService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.debit.DebitCardFactory;
import ru.bank.persistence.factory.cards.debit.DebitCardFactoryStandard;

import java.time.LocalDateTime;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class DebitCardServiceTest {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private UserService userService;


    @Test
    public void createDebitCardTest() {
        User user = userService.loadUserByLogin("test");
        DebitCardFactory debitCardFactory = new DebitCardFactoryStandard();
        DebitCard card = debitCardService.createDebitCard(user, debitCardFactory);
        Assert.assertTrue(card.getCardNumber().startsWith("11112"));
        Assert.assertTrue(card.getCloseDate().isAfter(LocalDateTime.now().plusMonths(35)));
        Assert.assertTrue(card.getCloseDate().isBefore(LocalDateTime.now().plusMonths(37)));
        Assert.assertEquals(0, card.getBalance(), 0.0);
        Assert.assertEquals("test", card.getUser().getLogin());
    }


    @Test
    public void findByUserTest() {
        User user = userService.loadUserByLogin("test");
        List<DebitCard> cards = debitCardService.findByUser(user);
        Assert.assertNotNull(cards);
    }

    @Test
    public void findByIdTest() {
        DebitCard card = debitCardService.findById(1L);
        Assert.assertNotNull(card);
        Assert.assertEquals(card.getId(), 1L);
        Assert.assertEquals(card.getUser().getLogin(), "test");
    }

    @Test
    public void increaseBalanceTest() {
        DebitCard card = debitCardService.findById(1L);
        double before = card.getBalance();
        debitCardService.increaseBalance(card, 1000.0);
        Assert.assertEquals(before + 1000, card.getBalance(), 0);

    }
    @Test
    public void decreaseBalanceTest() {
        DebitCard card = debitCardService.findById(1L);
        double before = card.getBalance();
        debitCardService.decreaseBalance(card, 1000.0);
        Assert.assertEquals(before - 1000, card.getBalance(), 0);
    }


}