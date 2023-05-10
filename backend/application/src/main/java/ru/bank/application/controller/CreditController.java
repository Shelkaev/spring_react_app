package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.credit.CreditApiRequest;
import ru.bank.application.dto.credit.CreditDetailsDto;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;
import ru.bank.application.service.credit.CreditService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.user.User;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/api/credit")
public class CreditController {
    @Autowired
    UserService userService;
    @Autowired
    CreditService creditService;

    @PostMapping("/details")
    public ResponseEntity outputTransferDetails(@CurrentUser JwtUser jwtUser, @Valid @RequestBody CreditApiRequest request) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            CreditDetailsDto creditDetails = creditService.getCreditDetails(user, request);
            log.info("IN TransferController.outputTransferDetails() - Details was output") ;
            return ResponseEntity.ok(creditDetails);
        } catch (RuntimeException e) {
            log.info("IN TransferController.outputTransferDetails() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get")
    public ResponseEntity getCredit(@CurrentUser JwtUser jwtUser, @Valid @RequestBody CreditDetailsDto request) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            creditService.getCredit(user, request);
            log.info("IN TransferController.getCredit() - Success") ;
            return ResponseEntity.ok(new BaseApiResponse(true, "Операция прошла успешно"));
        } catch (RuntimeException e) {
            log.info("IN TransferController.getCredit() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }
}
