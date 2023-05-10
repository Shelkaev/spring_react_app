package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.transfer.TransferAdminApiRequest;
import ru.bank.application.api.data.transfer.TransferApiRequest;
import ru.bank.application.api.data.transfer.TransferExecuteApiRequest;
import ru.bank.application.dto.transfer.BlockedTransferDto;
import ru.bank.application.dto.transfer.BlockedTransferDtoFactory;
import ru.bank.application.dto.transfer.TransferDetailsDto;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;
import ru.bank.application.service.transfer.cards.debit.DebitCardTransferService;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.user.User;

import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/transfer")
public class TransferController {
    @Autowired
    DebitCardTransferService transferService;
    @Autowired
    UserService userService;



    @PostMapping("/debit/details")
    public ResponseEntity outputTransferDetails(@Valid @RequestBody TransferApiRequest request, @CurrentUser JwtUser jwtUser) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            TransferDetailsDto transferDetails = transferService.getTransferDetails(user, request);
            log.info("IN TransferController.outputTransferDetails() - Details was output") ;
            return ResponseEntity.ok(transferDetails);
        } catch (RuntimeException e) {
            log.info("IN TransferController.outputTransferDetails() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/debit/execute")
    public ResponseEntity transferDebitPost(@CurrentUser JwtUser jwtUser, @Valid @RequestBody TransferExecuteApiRequest request) {
        try {
            User user = userService.loadUserByLogin(jwtUser.getLogin());
            transferService.executeTransfer(user, request);
            log.info("IN TransferController.transferDebitPost() - Transfer is done");
            return ResponseEntity.ok(new BaseApiResponse(true, "Перевод выполнен"));
        } catch (RuntimeException e) {
            log.info("IN TransferController.transferDebitPost() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("admin/blocked")
    public ResponseEntity getBlockedTransfers() {
        try {
        List<BlockedTransferDto> transfers = BlockedTransferDtoFactory
                                                .createTransferDtoList(transferService.getBlockedTransfers());
        log.info("IN TransferController.getBlockedTransfers() - Received a new GET request");
        return ResponseEntity.ok(transfers);
        } catch (RuntimeException e) {
            log.info("IN TransferController.getBlockedTransfers() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("admin/blocked/execute")
    public ResponseEntity executeBlockedTransfers(@Valid @RequestBody TransferAdminApiRequest request) {
        try {
            transferService.executeTransferAdmin(request.getTransferId());
            List<BlockedTransferDto> transfers = BlockedTransferDtoFactory
                    .createTransferDtoList(transferService.getBlockedTransfers());
            log.info("IN TransferController.executeBlockedTransfers() - Transfer was execute");
            return ResponseEntity.ok(transfers);
        }catch (RuntimeException e) {
            log.info("IN TransferController.executeBlockedTransfers() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("admin/blocked/reject")
    public ResponseEntity rejectBlockedTransfers(@Valid @RequestBody TransferAdminApiRequest request) {
        try {
            transferService.rejectTransferAdmin(request.getTransferId());
            List<BlockedTransferDto> transfers = BlockedTransferDtoFactory
                    .createTransferDtoList(transferService.getBlockedTransfers());
            log.info("IN TransferController.rejectBlockedTransfers() - Transfer was rejected");
            return ResponseEntity.ok(transfers);
        }catch (RuntimeException e) {
            log.info("IN TransferController.rejectBlockedTransfers() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

}
