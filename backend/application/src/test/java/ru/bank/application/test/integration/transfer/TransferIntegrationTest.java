package ru.bank.application.test.integration.transfer;

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
import ru.bank.application.api.data.transfer.TransferAdminApiRequest;
import ru.bank.application.api.data.transfer.TransferApiRequest;
import ru.bank.application.api.data.transfer.TransferExecuteApiRequest;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.exception.transfer.TransferNotFoundException;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.entity.user.Role;
import ru.bank.persistence.factory.transfer.commision.CommissionFactory;
import ru.bank.persistence.factory.transfer.type.TypeFactory;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class TransferIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private DebitCardTransferService transferService;
    @Autowired
    private DebitCardRepository debitCardRepository;


    private final String URL = "/api/transfer";
    private String token;


    @Before
    public void generateToken() {
        token = tokenProvider.createToken("test", Role.USER);
    }

    @Test
    public void accessDeniedTest() throws Exception {
        mvc.perform(get(URL + "/admin/blocked").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void accessDeniedUserTest() throws Exception {
        mvc.perform(get(URL + "/admin/blocked").header("Authorization", "Bearer_"
                                + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void internalToDebitCard_TransferDetailsTest() throws Exception {
        TransferApiRequest request = new TransferApiRequest();
        request.setAmount(1000.0);
        request.setCardId(1L);
        request.setBeneficiaryAccount("1111211111111112");
        mvc.perform(post(URL + "/debit/details").header("Authorization", "Bearer_" + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("INTERNAL_TO_DEBIT_CARD")))
                .andExpect(jsonPath("$.status", is("DRAFT")))
                .andExpect(jsonPath("$.commission", is(0.0)))
                .andExpect(jsonPath("$.amount", is(1000.0)))
                .andExpect(jsonPath("$.recipientAccount", is("1111211111111112")));

    }

    @Test
    public void executeTransferTest() throws Exception {
        Transfer transfer = transferRepository
                .findLastDraftTransferNative().orElseThrow(TransferNotFoundException::new);
        TransferExecuteApiRequest request = new TransferExecuteApiRequest();
        request.setTransferId(transfer.getId());
        request.setDebitCardId(transfer.getDebitCard().getId());

        mvc.perform(post(URL + "/debit/execute").header("Authorization", "Bearer_" + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Перевод выполнен")));

    }

    @Test
    public void getBlockedTransfersTest() throws Exception {
        TransferApiRequest request = new TransferApiRequest();
        request.setAmount(1000.0);
        request.setCardId(1L);
        request.setBeneficiaryAccount("1111211111111112");
        mvc.perform(post(URL + "/debit/details").header("Authorization", "Bearer_" + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is("INTERNAL_TO_DEBIT_CARD")))
                .andExpect(jsonPath("$.status", is("DRAFT")))
                .andExpect(jsonPath("$.commission", is(0.0)))
                .andExpect(jsonPath("$.amount", is(1000.0)))
                .andExpect(jsonPath("$.recipientAccount", is("1111211111111112")));
    }

    @Test
    public void executeBlockedTransferTest() throws Exception {
        createBlockedTransfer();
        token = tokenProvider.createToken("admin", Role.ADMIN);
        TransferAdminApiRequest request = new TransferAdminApiRequest();
        List<Transfer> transfers = transferRepository
                .findBlockedTransfersNative().orElseThrow(TransferNotFoundException::new);
        Transfer transfer = transfers.get(0);
        request.setTransferId(transfer.getId());
        mvc.perform(post(URL + "/admin/blocked/execute").header("Authorization", "Bearer_" + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void rejectBlockedTransferTest() throws Exception {
        createBlockedTransfer();
        token = tokenProvider.createToken("admin", Role.ADMIN);
        TransferAdminApiRequest request = new TransferAdminApiRequest();
        List<Transfer> transfers = transferRepository
                .findBlockedTransfersNative().orElseThrow(TransferNotFoundException::new);
        Transfer transfer = transfers.get(0);
        request.setTransferId(transfer.getId());
        mvc.perform(post(URL + "/admin/blocked/reject").header("Authorization", "Bearer_" + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    private void createBlockedTransfer() {
        DebitCard card = debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        Transfer transfer = new Transfer();
        transfer.setDebitCard(card);
        transfer.setRecipientAccount("6666229199296722");
        transfer.setAmount(50.0);
        transfer.setStatus(TransferStatus.BLOCK);
        transfer.setType(TypeFactory.getType("6666229199296333"));
        transfer.setCommission(CommissionFactory.calculateTransferCommission(transfer.getType()));
        transferRepository.save(transfer);
    }

}


