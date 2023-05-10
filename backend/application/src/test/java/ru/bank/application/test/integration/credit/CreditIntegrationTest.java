package ru.bank.application.test.integration.credit;

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
import ru.bank.application.api.data.credit.CreditApiRequest;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.credit.CreditService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.repository.card.DebitCardRepository;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class CreditIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CreditService creditService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private DebitCardRepository debitCardRepository;

    private final String URL = "/api/credit";
    private String token;

    @Before
    public void generateToken() {
        token = tokenProvider.createToken("test", Role.USER);
    }

    @Test
    public void postCreditDetailsRU_thenStatus200() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(200);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("RU");

        mvc.perform(post(URL + "/details").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCommission", is(0.0)))
                .andExpect(jsonPath("$.creditPercent", is(cards.get(0).getCreditPercent())))
                .andExpect(jsonPath("$.amount", is(200.0)))
                .andExpect(jsonPath("$.currency", is("RU")));
    }

    @Test
    public void postCreditDetailsUSD_thenStatus200() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(2);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("USD");

        mvc.perform(post(URL + "/details").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCommission", is(cards.get(0).getCurrencyPercent())))
                .andExpect(jsonPath("$.creditPercent", is(cards.get(0).getCreditPercent())))
                .andExpect(jsonPath("$.amount", is(2.0)))
                .andExpect(jsonPath("$.currency", is("USD")));
    }

    @Test
    public void postTakeCreditRU_thenStatus200() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(200);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("RU");
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);

        mvc.perform(post(URL + "/get").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(creditDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Операция прошла успешно")));

    }

    @Test
    public void postTakeCreditEUR_thenStatus200() throws Exception {
        User user = userService.loadUserByLogin("test");
        List<CreditCard> cards = creditCardService.findActiveCardsByUser(user);
        CreditApiRequest request = new CreditApiRequest();
        request.setAmount(2);
        request.setCardId(cards.get(0).getId());
        request.setCurrency("EUR");
        CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);

        mvc.perform(post(URL + "/get").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(creditDetails)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Операция прошла успешно")));

    }

}
