package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.cards.CardIdApiRequest;
import ru.bank.application.api.data.cards.CreditCardСonditionsApiRequest;
import ru.bank.application.api.data.cards.EditCreditCardApiRequest;
import ru.bank.application.api.data.cards.PenniApiRequest;
import ru.bank.application.dto.card.CardFactoryDto;
import ru.bank.application.dto.card.CreditCardDto;
import ru.bank.application.dto.card.OrderedCreditCardsDto;
import ru.bank.application.dto.credit.CreditDto;
import ru.bank.application.dto.credit.CreditDtoFactory;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;
import ru.bank.application.service.cards.credit.CreditCardService;
import ru.bank.application.service.credit.CreditService;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.card.CreditCard;
import ru.bank.persistence.entity.user.User;
import ru.bank.persistence.factory.cards.credit.CreditCardFactory;
import ru.bank.persistence.factory.cards.credit.CreditCardFactoryStandard;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/cards/credit")
public class CreditCardController {

    @Autowired
    private UserService userService;
    @Autowired
    private DebitCardTransferService transferService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private CreditService creditService;


    @GetMapping
    public ResponseEntity getAllCreditCards(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            List<CreditCardDto> creditCards = CardFactoryDto.createCreditCardList(creditCardService.findByUser(user));
            log.info("IN CreditCardController.getAllCreditCards() - Credit cards for user {} are loaded", user.getLogin());
            return ResponseEntity.ok(creditCards);
        } catch (RuntimeException e) {
            log.info("IN CreditCardController.getAllCreditCards() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }
    @GetMapping("/active")
    public ResponseEntity getActiveCreditCards(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            List<CreditCardDto> creditCards =
                    CardFactoryDto.createCreditCardList(creditCardService.findActiveCardsByUser(user));
            log.info("IN CreditCardController.getActiveCreditCards() - Credit cards for user {} are loaded", user.getLogin());
            return ResponseEntity.ok(creditCards);
        } catch (RuntimeException e) {
            log.info("IN CreditCardController.getActiveCreditCards() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/{cardId}")
    public ResponseEntity getCreditCard(@CurrentUser JwtUser jwtUser, @PathVariable(value = "cardId") Long cardId) {
        try {
            CreditCard card = getUsersCreditCard(jwtUser.getLogin(), cardId);
            log.info("IN CreditCardController.getCreditCard() - Credit card with id {} are loaded", cardId);
            return ResponseEntity.ok(CardFactoryDto.crateCreditCard(card));
        } catch (RuntimeException e) {
            log.info("IN CreditCardController.getCreditCard() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/order")
    public ResponseEntity createCreditCard(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            CreditCardFactory creditCardFactory = new CreditCardFactoryStandard();
            CreditCard creditCard = creditCardService.createCreditCard(user, creditCardFactory);
            List<CreditCardDto> creditCards = CardFactoryDto.createCreditCardList(creditCardService.findByUser(user));
            log.info("IN createCreditCard() - Credit card a number {} was created", creditCard.getCardNumber());
            return ResponseEntity.ok(creditCards);
        } catch (RuntimeException e) {
            log.info("IN createCreditCard() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_IMPLEMENTED);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity editCreditCard(@CurrentUser JwtUser jwtUser,
                                         @Valid @RequestBody EditCreditCardApiRequest request) {
        try {
            CreditCard card = getUsersCreditCard(jwtUser.getLogin(), request.getCardId());
            creditCardService.changeStateCreditCard(card, request.getStatus());
            log.info("IN editCreditCard() - CreditCard with a number {} was edit", card.getCardNumber());
            return ResponseEntity.ok(new BaseApiResponse(true,"Изменения внесены"));
        } catch (RuntimeException e) {
            log.info("IN editCreditCard() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/penni")
    public ResponseEntity editPenniRequest(@CurrentUser JwtUser jwtUser,
                                           @Valid @RequestBody CardIdApiRequest request) {
        try {
            CreditCard card = getUsersCreditCard(jwtUser.getLogin(), request.getCardId());
            creditCardService.changePenniNote(card);
            log.info("IN editPenniRequest() - CreditCard with a number {} requested", card.getCardNumber());
            return ResponseEntity.ok(new BaseApiResponse(true,"Запрос принят"));
        } catch (RuntimeException e) {
            log.info("IN editPenniRequest() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/admin/parameters")
    public ResponseEntity setParameters(@Valid @RequestBody CreditCardСonditionsApiRequest request) {
        try {
            List<CreditCard> cards = creditCardService.getOrderedCreditCards();
            CreditCard card = creditCardService.findCardById(cards, request.getCardId());
            creditCardService.setParameters(card, request);
            log.info("IN setParameters() - Balance for creditCard with id {} was increased in {}",
                    request.getCardId(), request.getLimit());
            return ResponseEntity.ok(new BaseApiResponse(true, "Операция выполнена"));
        } catch (RuntimeException e) {
            log.info("IN setParameters() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/admin/ordered")
    public ResponseEntity getOrderedCards() {
        try {
            List<OrderedCreditCardsDto> cards =
                    CardFactoryDto.createOrderedCreditCardsDtoList(creditCardService.getOrderedCreditCards());
            log.info("IN getOrderedCards() - Get request was received");
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            log.info("IN getOrderedCards() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/admin/penni")
    public ResponseEntity getCardsWithChangeRequest() {
        try {
            List<OrderedCreditCardsDto> cards =
                    CardFactoryDto.createOrderedCreditCardsDtoList(creditCardService.getChangePenniCards());
            log.info("IN getCardsWithChangeRequest() - Get request was received");
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            log.info("IN getCardsWithChangeRequest() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/admin/penni")
    public ResponseEntity setPenni(@Valid @RequestBody PenniApiRequest request) {
        try {
            List<CreditCard> cards = creditCardService.getChangePenniCards();
            CreditCard card = creditCardService.findCardById(cards, request.getCardId());
            creditCardService.setPenni(card, request);
            log.info("IN setPenni() - Penni for creditCard with id {} was change in {}",
                    request.getCardId(), request.getPenniPercent());
            return ResponseEntity.ok(new BaseApiResponse(true, "Операция выполнена"));
        } catch (RuntimeException e) {
            log.info("IN setPenni() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/admin/{cardId}")
    public ResponseEntity getOrderedCard(@PathVariable(value = "cardId") Long cardId) {
        try {
            CreditCardDto card = CardFactoryDto.crateCreditCard(creditCardService.findById(cardId));
            log.info("IN getOrderedCard() - Get request was received");
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            log.info("IN getOrderedCard() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }


    @GetMapping("/output/history/{cardId}")
    public ResponseEntity creditHistory(@CurrentUser JwtUser jwtUser,
                                                @PathVariable(value = "cardId") Long cardId) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            CreditCard card = creditCardService.getUserCardById(user, cardId);
            List<CreditDto> credits = CreditDtoFactory.createCreditDtoList(creditService.getActiveCredits(card), card);
            log.info("IN creditHistory() - Done");
            return ResponseEntity.ok(credits);
        } catch (RuntimeException e) {
            log.info("IN creditHistory() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }


    private CreditCard getUsersCreditCard(String login, long cardId) {
        User user = userService.loadUserByLogin(login);
        return creditCardService.getUserCardById(user, cardId);
    }


}

