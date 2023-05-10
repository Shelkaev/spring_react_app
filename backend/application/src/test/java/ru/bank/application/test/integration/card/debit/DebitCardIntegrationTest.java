package ru.bank.application.test.integration.card.debit;

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
import ru.bank.application.api.data.cards.IncreasedBalanceApiRequest;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.cards.debit.DebitCardService;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.repository.card.DebitCardRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class DebitCardIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private DebitCardRepository debitCardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DebitCardService debitCardService;
    @Autowired
    private DebitCardTransferService transferService;


    private final String URL = "/api/cards/debit";
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
    public void accessDeniedAdminTest() throws Exception {
        token = tokenProvider.createToken("admin", Role.ADMIN);
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createDebitCard_thenStatus200() throws Exception {
        String jsonExpression = String.format("$[%d].cardType", getCardListSizeTestUser());
        mvc.perform(post(URL + "/create").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(jsonExpression, is("Debit")));

    }


    @Test
    public void getAllDebitCards_thenStatus200() throws Exception {
        String jsonExpression = String.format("$[%d].cardType", getCardListSizeTestUser() - 1);
        mvc.perform(get(URL).header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath(jsonExpression, is("Debit")));
    }

    @Test
    public void getOneDebitCard_thenStatus200() throws Exception {
        DebitCard card = debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        mvc.perform(get(URL + "/1").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.cardType", is("Debit")))
                .andExpect(jsonPath("$.cardNumber", is(muteCardNumber(card.getCardNumber()))));
    }

    @Test
    public void increaseBalanceDebitCard_thenStatus200() throws Exception {
        IncreasedBalanceApiRequest request = new IncreasedBalanceApiRequest();
        request.setAmount(1000.0);
        request.setCardId(1L);
        DebitCard card = debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        double balance = card.getBalance();
        mvc.perform(post(URL + "/increase").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(balance + 1000.0)));
    }

    @Test
    public void getDebitCardHistoryTest() throws Exception {
        List<Transfer> transfers = transferService.getOutputHistory(1);
        String jsonExpression = String.format("$[%d]", transfers.size() - 1);
        DebitCard card = debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);

        mvc.perform(get(URL + "/transfers/output/history/1").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(jsonExpression + ".debitCardId", is(1)))
                .andExpect(jsonPath(jsonExpression + ".account", is(muteCardNumber(card.getCardNumber()))));
    }

    private String muteCardNumber(String cardNumber) {
        Matcher matcher = Pattern.compile("\\d{4}$").matcher(cardNumber);
        matcher.find();
        return "**** **** **** " + matcher.group();
    }

    private int getCardListSizeTestUser() {
        User user = userService.loadUserByLogin("test");
        List<DebitCard> cards = debitCardRepository.findByUser(user).orElseThrow(CardNotFoundException::new);
        return cards.size();
    }


}

