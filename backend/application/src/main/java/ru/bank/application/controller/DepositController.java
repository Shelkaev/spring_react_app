package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.deposit.CloseDepositApiRequest;
import ru.bank.application.api.data.deposit.CreateDepositApiRequest;
import ru.bank.application.api.data.deposit.DecreaseDepositApiRequest;
import ru.bank.application.dto.deposit.DepositDto;
import ru.bank.application.dto.deposit.DepositFactoryDto;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;
import ru.bank.application.service.deposit.DepositService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.user.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity getDeposits(@CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            List<DepositDto> rates = DepositFactoryDto.createDebitCardList(depositService.getDeposits(user));
            log.info("IN getDeposits() - Get request was received");
            return ResponseEntity.ok(rates);
        } catch (RuntimeException e) {
            log.info("IN getDeposits() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }
    @GetMapping("/{depositId}")
    public ResponseEntity getOneDeposit(@CurrentUser JwtUser jwtUser, @PathVariable(value = "depositId") Long depositId) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            DepositDto deposit = DepositFactoryDto.crateDeposit(depositService.getOneDeposit(user, depositId));
            log.info("IN getOneDeposit() - Get request was received");
            return ResponseEntity.ok(deposit);
        } catch (RuntimeException e) {
            log.info("IN getOneDeposit() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/create")
    public ResponseEntity createDeposit(@CurrentUser JwtUser jwtUser, @Valid @RequestBody CreateDepositApiRequest request) {
            try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            depositService.createDeposit(user, request.getRateId());
            log.info("IN createDeposit() - Deposit was created");
            return ResponseEntity.ok(new BaseApiResponse(true,"Депозит открыт"));
        } catch (RuntimeException e) {
            log.info("IN createDeposit() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/close")
    public ResponseEntity closeDeposit(@CurrentUser JwtUser jwtUser, @Valid @RequestBody CloseDepositApiRequest request) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            depositService.closeDeposit(user, request.getDepositId(), request.getDebitCardId());
            log.info("IN closeDeposit() - Deposit with id {} was closed", request.getDepositId());
            return ResponseEntity.ok(new BaseApiResponse(true,"Депозит закрыт"));
        } catch (RuntimeException e) {
            log.info("IN closeDeposit() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false,e.getMessage()));
        }
    }

    @PostMapping("/decrease")
    public ResponseEntity decreaseDeposit(@CurrentUser JwtUser jwtUser,
                                          @Valid @RequestBody DecreaseDepositApiRequest request) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            depositService.decreaseDeposit(user, request.getDepositId(), request.getDebitCardId(), request.getAmount());
            log.info("IN decreaseDeposit() - Deposit with id {} was decreased in {}",
                    request.getDepositId(), request.getAmount());
            return ResponseEntity.ok(new BaseApiResponse(true,"Перевод выполнен"));
        } catch (RuntimeException e) {
            log.info("IN decreaseDeposit() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false,e.getMessage()));
        }
    }



}
