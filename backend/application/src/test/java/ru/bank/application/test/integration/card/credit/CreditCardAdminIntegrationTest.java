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
import ru.bank.application.api.data.cards.CreditCardСonditionsApiRequest;
import ru.bank.application.api.data.cards.PenniApiRequest;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CardState;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;
import ru.bank.persistence.factory.cards.credit.CreditCardFactoryStandard;
import ru.bank.persistence.repository.card.CreditCardRepository;

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
public class CreditCardAdminIntegrationTest {

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


    private final String URL = "/api/cards/credit";
    private String token;

    @Before
    public void generateToken() {
        token = tokenProvider.createToken("admin", Role.ADMIN);
    }

    @Test
    public void accessDeniedTest() throws Exception {
        mvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void accessDeniedAdminTest() throws Exception {
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    public void setParametersAdminTest() throws Exception {
        CreditCardСonditionsApiRequest request = generateСonditionsApiRequest();
        CreditCard card = creditCardRepository.findAll()
                .stream()
                .filter(c -> c.getState() == CardState.ORDERED)
                .findFirst().orElseThrow(CardNotFoundException::new);
        request.setCardId(card.getId());

        mvc.perform(post(URL + "/admin/parameters").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Операция выполнена")));

    }

    @Test
    public void getOrderedCardsAdminTest() throws Exception {
        User user = userService.loadUserByLogin("test");
        orderCreditCard(user);

        mvc.perform(get(URL + "/admin/ordered").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getCardsWithChangeRequestAdminTest() throws Exception {
        User user = userService.loadUserByLogin("test");
        CreditCard card = orderCreditCard(user);
        creditCardService.setParameters(card, generateСonditionsApiRequest());
        creditCardService.changePenniNote(card);

        mvc.perform(get(URL + "/admin/penni").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userLogin", is("test")));
    }

    @Test
    public void getCreditCardAdminTest() throws Exception {
        mvc.perform(get(URL + "/admin/1").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardType", is("Credit")));
    }

    @Test
    public void setPenniAdminTest() throws Exception {
        CreditCard card = generateCreditCard();
        creditCardService.changePenniNote(card);
        PenniApiRequest request = new PenniApiRequest();
        request.setCardId(card.getId());
        request.setCurrencyPercent(0.01);
        request.setPenniPercent(0.001);

        mvc.perform(post(URL + "/admin/penni").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Операция выполнена")));

    }


    private CreditCardСonditionsApiRequest generateСonditionsApiRequest() {
        CreditCardСonditionsApiRequest request = new CreditCardСonditionsApiRequest();
        request.setLimit(50000);
        request.setCurrencyPercent(0.1);
        request.setCreditPercent(0.2);
        request.setPenniPercent(0.01);
        return request;
    }

    private CreditCard orderCreditCard(User user) {
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        return creditCardService.createCreditCard(user, cardFactory);
    }

    private CreditCard generateCreditCard() {
        User user = userService.loadUserByLogin("test");
        CreditCardFactory cardFactory = new CreditCardFactoryStandard();
        CreditCard card = creditCardService.createCreditCard(user, cardFactory);
        CreditCardСonditionsApiRequest request = new CreditCardСonditionsApiRequest();
        request.setLimit(50000);
        request.setCurrencyPercent(0.1);
        request.setCreditPercent(0.2);
        request.setPenniPercent(0.01);
        creditCardService.setParameters(card, request);
        creditCardService.changeStateCreditCard(card, "ACTIVE");
        return creditCardRepository.findById(card.getId()).orElseThrow(CardNotFoundException :: new);
    }


}

