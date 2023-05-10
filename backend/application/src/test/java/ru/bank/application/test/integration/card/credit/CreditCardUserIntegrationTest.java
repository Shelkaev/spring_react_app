package ru.bank.application.test.integration.card.credit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.application.Application;
import ru.bank.application.api.data.cards.CardIdApiRequest;
import ru.bank.application.api.data.cards.CreditCardСonditionsApiRequest;
import ru.bank.application.api.data.cards.EditCreditCardApiRequest;
import ru.bank.application.api.data.credit.CreditApiRequest;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.credit.CreditService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;
import ru.bank.persistence.factory.cards.credit.CreditCardFactoryStandard;
import ru.bank.persistence.repository.card.CreditCardRepository;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class CreditCardUserIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private CreditService creditService;


    private final String URL = "/api/cards/credit";
    private String token;

    @Before
    public void generateToken() {
        token = tokenProvider.createToken("test", Role.USER);
    }

    @Test
    public void accessDeniedTest() throws Exception {
        mvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    public void createCreditCard_thenStatus200Test() throws Exception {
        String jsonExpression = String.format("$[%d]", getCardListSize());
        User user = userService.loadUserByLogin("test");
        orderCreditCard(user);
        mvc.perform(post(URL + "/order").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(jsonExpression + ".cardType", is("Credit")))
                .andExpect(jsonPath(jsonExpression + ".balance", is(0.0)));
    }

    @Test
    public void getAllCreditCards_thenStatus200Test() throws Exception {
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardType", is("Credit")));
    }

    @Test
    public void getActiveCreditCards_thenStatus200Test() throws Exception {
        mvc.perform(get(URL + "/active").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardState", is("Активна")));
    }

    @Test
    public void getCreditCard_thenStatus200Test() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        mvc.perform(get(URL + "/" + cards.get(0).getId()).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardType", is("Credit")));
    }

    @Test
    public void rejectCreditCard_thenStatus200Test() throws Exception {
        generateProposedCard();
        EditCreditCardApiRequest request = new EditCreditCardApiRequest();
        User user = userService.loadUserByLogin("test");
        CreditCard card = creditCardService.findByUser(user)
                .stream()
                .filter(c -> c.getState() == CardState.PROPOSED)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        request.setCardId(card.getId());
        request.setStatus("REJECTED");
        mvc.perform(post(URL + "/edit").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Изменения внесены")));
    }

    @Test
    public void activateCreditCard_thenStatus200Test() throws Exception {
        generateProposedCard();
        EditCreditCardApiRequest request = new EditCreditCardApiRequest();
        User user = userService.loadUserByLogin("test");
        CreditCard card = creditCardService.findByUser(user)
                .stream()
                .filter(c -> c.getState() == CardState.PROPOSED)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        request.setCardId(card.getId());
        request.setStatus("ACTIVE");
        mvc.perform(post(URL + "/edit").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Изменения внесены")));
    }

    @Test
    public void changePenniRequestTest() throws Exception {
        User user = userService.loadUserByLogin("test");
        CreditCard card = creditCardService.findActiveCardsByUser(user)
                .stream()
                .filter(c -> c.getNote() == null)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        CardIdApiRequest request = new CardIdApiRequest();
        request.setCardId(card.getId());

        mvc.perform(post(URL + "/penni").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Запрос принят")));
    }

    @Test
    public void getCreditCardHistory_thenStatus200Test() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(2);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("USD");
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
        creditService.getCredit(user, creditDetails);

        mvc.perform(get(URL + "/output/history/" + cards.get(0).getId()).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    private int getCardListSize() {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findByUser(user);
        return cards.size();
    }

    private void generateProposedCard() {
        User user = userService.loadUserByLogin("test");
        orderCreditCard(user);
        CreditCard card = creditCardService.findByUser(user)
                .stream()
                .filter(c -> c.getState() == CardState.ORDERED)
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        creditCardService.setParameters(card, generateСonditionsApiRequest());
    }

    private CreditCardСonditionsApiRequest generateСonditionsApiRequest() {
        CreditCardСonditionsApiRequest request = new CreditCardСonditionsApiRequest();
        request.setLimit(50000);
        request.setCurrencyPercent(0.1);
        request.setCreditPercent(0.2);
        request.setPenniPercent(0.01);
        return request;
    }

    private void orderCreditCard(User user) {
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        creditCardService.createCreditCard(user, cardFactory);
    }


}

