package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.rate.RateApiRequest;
import ru.bank.application.api.data.rate.RateIdApiRequest;
import ru.bank.application.dto.rate.RateDto;
import ru.bank.application.service.rate.RateService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/rate")
public class RateController {

    @Autowired
    private RateService rateService;

    @GetMapping("get/rates")
    public ResponseEntity getRates() {
        try {
            List<RateDto> rates = rateService.getRates();
            log.info("IN getRates() - Get request was received");
            return ResponseEntity.ok(rates);
        } catch (RuntimeException e) {
            log.info("IN getRates() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("get/rates/{rateId}")
    public ResponseEntity getOneRate(@PathVariable(value = "rateId") Long rateId) {
        try {
            RateDto rate = rateService.getOneRate(rateId);
            log.info("IN getOneRate() - Get request was received");
            return ResponseEntity.ok(rate);
        } catch (RuntimeException e) {
            log.info("IN getOneRate() - Exception : {}", e.getMessage());
            return new ResponseEntity(new BaseApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("admin/create")
    public ResponseEntity createRate(@Valid @RequestBody RateApiRequest request) {
        try {
            rateService.createRate(request);
            log.info("IN createRate() - Rate with name {} was created", request.getName());
            return ResponseEntity.ok(new BaseApiResponse(true, "Тариф создан"));
        } catch (RuntimeException e) {
            log.info("IN createRate() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("admin/change")
    public ResponseEntity changeRate(@Valid @RequestBody RateApiRequest request) {
        try {
            rateService.changeRate(request);
            log.info("IN changeRate() - Rate with name {} was created", request.getName());
            return ResponseEntity.ok(new BaseApiResponse(true, "Изменения внесены"));
        } catch (RuntimeException e) {
            log.info("IN changeRate() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("admin/close")
    public ResponseEntity closeRate(@Valid @RequestBody RateIdApiRequest request) {
        try {
            rateService.closeRate(request);
            log.info("IN closeRate() - Rate with id {} was deleted", request.getId());
            return ResponseEntity.ok(new BaseApiResponse(true, "Тариф перемещен в архив"));
        } catch (RuntimeException e) {
            log.info("IN closeRate() - Exception : {}", e.getMessage());
            return ResponseEntity.ok(new BaseApiResponse(false, e.getMessage()));
        }
    }

}
