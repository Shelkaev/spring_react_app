package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.cards.IncreaseBalanceApiResponse;
import ru.bank.application.api.data.cards.IncreasedBalanceApiRequest;
import ru.bank.application.dto.card.CardFactoryDto;
import ru.bank.application.dto.card.DebitCardDto;
import ru.bank.application.dto.transfer.TransferDetailsDto;
import ru.bank.application.dto.transfer.TransferDetailsDtoFactory;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;
import ru.bank.application.service.cards.debit.DebitCardService;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.DebitCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.debit.DebitCardFactoryStandard;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/cards/debit")
public class DebitCardController {

    @Autowired
    private UserService userService;
    @Autowired
    private DebitCardService debitCardService;
    @Autowired
    private DebitCardTransferService transferService;


    @GetMapping
    public ResponseEntity getAllDebitCards(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            List<DebitCardDto> debitCards = CardFactoryDto.createDebitCardList(debitCardService.findByUser(user));
            log.info("IN DebitCardController.getAllDebitCards() - Debit cards for user {} are loaded", user.getLogin());
            return ResponseEntity.ok(debitCards);
        } catch (RuntimeException e) {
            log.info("IN DebitCardController.getAllDebitCards() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{cardId}")
    public ResponseEntity getDebitCard(@CurrentUser JwtUser jwtUser, @PathVariable(value = "cardId") Long cardId) {
        try {
            DebitCard card = getUsersDebitCard(jwtUser.getLogin(), cardId);
            log.info("IN DebitCardController.getDebitCard() - Debit card with id {} are loaded", cardId);
            return ResponseEntity.ok(CardFactoryDto.crateDebitCard(card));
        } catch (RuntimeException e) {
            log.info("IN DebitCardController.getDebitCard() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity createDebitCard(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            DebitCardFactoryStandard debitCardFactory = new DebitCardFactoryStandard();
            DebitCard debitCard = debitCardService.createDebitCard(user, debitCardFactory);
            List<DebitCardDto> debitCards = CardFactoryDto.createDebitCardList(debitCardService.findByUser(user));
            log.info("IN DebitCardController.createDebitCard() - DebitCard with a number {} was created", debitCard.getCardNumber());
            return ResponseEntity.ok(debitCards);
        } catch (RuntimeException e) {
            log.info("IN DebitCardController.createDebitCard() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/increase")
    public ResponseEntity increaseBalance(@CurrentUser JwtUser jwtUser,
                                          @Valid @RequestBody IncreasedBalanceApiRequest request) {
        try {
            DebitCard card = getUsersDebitCard(jwtUser.getLogin(), request.getCardId());
            debitCardService.increaseBalance(card, request.getAmount());
            log.info("IN DebitCardController.increaseBalance() - Balance for debitCard with id {} was increased in {}",
                    request.getCardId(), request.getAmount());
            return ResponseEntity.ok(new IncreaseBalanceApiResponse(card.getBalance()));
        } catch (RuntimeException e) {
            log.info("IN DebitCardController.increaseBalance() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transfers/output/history/{cardId}")
    public ResponseEntity transferOutputHistory(@CurrentUser JwtUser jwtUser,
                                                @PathVariable(value = "cardId") Long cardId) {
        try {
            DebitCard card = getUsersDebitCard(jwtUser.getLogin(), cardId);
            List<TransferDetailsDto> transfers = TransferDetailsDtoFactory
                    .createTransferDtoList(transferService.getOutputHistory(card.getId()));
            log.info("IN TransferController.transferOutputHistory() - Done");
            return ResponseEntity.ok(transfers);
        } catch (RuntimeException e) {
            log.info("IN TransferController.transferOutputHistory() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }

    
    private DebitCard getUsersDebitCard(String login, long cardId) {
        User user = userService.loadUserByLogin(login);
        return debitCardService.getUserCardById(user, cardId);
    }

}

