package ru.bank.application.test.module.transfer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.bank.application.Application;
import ru.bank.application.api.data.transfer.TransferApiRequest;
import ru.bank.application.api.data.transfer.TransferExecuteApiRequest;
import ru.bank.application.dto.transfer.TransferDetailsDto;
import ru.bank.application.exception.card.CardNotFoundException;
import ru.bank.application.exception.transfer.FraudMonitorException;
import ru.bank.application.exception.transfer.TransferNotFoundException;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.transfer.Transfer;
import ru.bank.persistence.entity.transfer.TransferStatus;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.transfer.commision.CommissionFactory;
import ru.bank.persistence.factory.transfer.type.TypeFactory;
import ru.bank.persistence.repository.card.DebitCardRepository;
import ru.bank.persistence.repository.transfer.TransferRepository;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
public class TransferServiceTest {
    @Autowired
    DebitCardTransferService transferService;
    @Autowired
    TransferRepository transferRepository;
    @Autowired
    UserService userService;
    @Autowired
    DebitCardRepository debitCardRepository;


    @Test
    public void getTransferDetailsFor_InternalToCreditCard_TransferTest() {
        User user = userService.loadUserByLogin("test");
        TransferApiRequest request = generateRequest("1111011122223333", 50);
        TransferDetailsDto transferDetails = transferService.getTransferDetails(user, request);
        Assert.assertEquals("INTERNAL_TO_CREDIT_CARD", transferDetails.getType());
        Assert.assertEquals("DRAFT", transferDetails.getStatus());
        Assert.assertEquals(0, transferDetails.getCommission(), 0);
        Assert.assertEquals(50, transferDetails.getAmount(), 0);
        Assert.assertEquals("1111011122223333", transferDetails.getRecipientAccount());
    }

    @Test
    public void getTransferDetailsFor_InternalToDeposit_TransferTest() {
        User user = userService.loadUserByLogin("test");
        TransferApiRequest request = generateRequest("44444111222233334444", 50);
        TransferDetailsDto transferDetails = transferService.getTransferDetails(user, request);
        Assert.assertEquals("INTERNAL_TO_DEPOSIT", transferDetails.getType());
        Assert.assertEquals("DRAFT", transferDetails.getStatus());
        Assert.assertEquals(0, transferDetails.getCommission(), 0);
        Assert.assertEquals(50, transferDetails.getAmount(), 0);
        Assert.assertEquals("44444111222233334444", transferDetails.getRecipientAccount());
    }


    @Test
    public void getTransferDetailsFor_External_TransferTest() {
        User user = userService.loadUserByLogin("test");
        TransferApiRequest request = generateRequest("6666111122223333", 50);
        DebitCard card = debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new);
        TransferDetailsDto transferDetails = transferService.getTransferDetails(user, request);
        Assert.assertEquals(1L, transferDetails.getDebitCardId());
        Assert.assertEquals("EXTERNAL", transferDetails.getType());
        Assert.assertEquals("DRAFT", transferDetails.getStatus());
        Assert.assertEquals(0.01, transferDetails.getCommission(), 0);
        Assert.assertEquals(50, transferDetails.getAmount(), 0);
        Assert.assertEquals(muteCardNumber(card.getCardNumber()), transferDetails.getAccount());
        Assert.assertEquals("6666111122223333", transferDetails.getRecipientAccount());
    }

    @Test
    public void getTransferDetailsFor_InternalToDebitCard_TransferTest() {
        User user = userService.loadUserByLogin("test");
        TransferApiRequest request = generateRequest("1111229199296722", 50);
        TransferDetailsDto transferDetails = transferService.getTransferDetails(user, request);
        Assert.assertEquals("INTERNAL_TO_DEBIT_CARD", transferDetails.getType());
        Assert.assertEquals("DRAFT", transferDetails.getStatus());
        Assert.assertEquals(0, transferDetails.getCommission(), 0);
        Assert.assertEquals(50, transferDetails.getAmount(), 0);
        Assert.assertEquals("1111229199296722", transferDetails.getRecipientAccount());
    }


    @Test
    public void execute_internal_TransferTest() {
        User user = userService.loadUserByLogin("test");
        transferService.getTransferDetails(user, generateRequest("6666229199296722", 50));
        Transfer transfer = transferRepository.findLastDraftTransferNative().orElseThrow(TransferNotFoundException::new);
        TransferExecuteApiRequest request = new TransferExecuteApiRequest();
        request.setTransferId(transfer.getId());
        request.setDebitCardId(1L);
        transferService.executeTransfer(user, request);
        Transfer transfer1 = transferRepository.findById(transfer.getId()).orElseThrow(TransferNotFoundException::new);
        Assert.assertEquals("DONE", transfer1.getStatus().name());
    }


    @Test
    public void getOutputHistoryTest() {
        List<Transfer> transfers = transferService.getOutputHistory(1L);
        for (Transfer transfer : transfers) {
            Assert.assertNotEquals(transfer.getStatus(), TransferStatus.DRAFT);
        }
    }


    @Test(expected = FraudMonitorException.class)
    public void fraudMonitorTransfersTest() {
        User user = userService.loadUserByLogin("test");
        TransferDetailsDto transferDetails =
                transferService.getTransferDetails(user,
                        generateRequest("1111229199296722", 60000));
        TransferExecuteApiRequest request = new TransferExecuteApiRequest();
        request.setTransferId(transferDetails.getId());
        request.setDebitCardId(1L);
        transferService.executeTransfer(user, request);
    }

    @Test
    public void getBlockedTransfersTest() {
        Transfer transfer = generateTransfer(TransferStatus.BLOCK);
        List<Transfer> transfers = transferService.getBlockedTransfers();
        for (Transfer t : transfers) {
            Assert.assertEquals(t.getStatus(), TransferStatus.BLOCK);
        }

    }

    @Test
    public void executeTransferAdminTest() {
        Transfer transfer = generateTransfer(TransferStatus.BLOCK);
        transferService.executeTransferAdmin(transfer.getId());
        Transfer transfer1 = transferRepository.findById(transfer.getId()).orElseThrow(TransferNotFoundException::new);
        Assert.assertEquals("DONE", transfer1.getStatus().name());
    }

    @Test
    public void rejectTransferAdminTest() {
        Transfer transfer = generateTransfer(TransferStatus.DRAFT);
        transferService.rejectTransferAdmin(transfer.getId());
        Transfer transfer1 = transferRepository.findById(transfer.getId()).orElseThrow(TransferNotFoundException::new);
        Assert.assertEquals("REJECTED", transfer1.getStatus().name());

    }


    private String muteCardNumber(String cardNumber) {
        Matcher matcher = Pattern.compile("\\d{4}$").matcher(cardNumber);
        matcher.find();
        return "**** **** **** " + matcher.group();
    }

    private TransferApiRequest generateRequest(String beneficiaryAccount, double amount) {
        User user = userService.loadUserByLogin("test");
        TransferApiRequest request = new TransferApiRequest();
        request.setCardId(1L);
        request.setBeneficiaryAccount(beneficiaryAccount);
        request.setAmount(amount);
        return request;
    }

    private Transfer generateTransfer(TransferStatus status) {
        Transfer transfer = new Transfer();
        transfer.setDebitCard(debitCardRepository.findById(1L).orElseThrow(CardNotFoundException::new));
        transfer.setRecipientAccount("1111229199296722");
        transfer.setAmount(60000);
        transfer.setStatus(TransferStatus.BLOCK);
        transfer.setType(TypeFactory.getType("1111229199296722"));
        transfer.setCommission(CommissionFactory.calculateTransferCommission(transfer.getType()));
        transferRepository.save(transfer);
        return transfer;
    }
}
