package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.application.api.data.login.GreetingApiResponse;
import ru.bank.application.security.CurrentUser;
import ru.bank.application.security.JwtUser;



@RestController
@Slf4j
@RequestMapping("/api")
public class MainController {


    @GetMapping
    public ResponseEntity main(@CurrentUser JwtUser jwtUser){
        String title = jwtUser.getName() + " " + jwtUser.getPatronymic() + ", " +
                "Добрый день!";
        String body = "Уважаемый клиент, в связи с проведением технологических работ ряд операций недоступен" +
                " в полном объеме, приносим извинения за доставленные неудобства.";
        GreetingApiResponse apiResponse = new GreetingApiResponse(title, body);
        log.info("IN MainController.main() - Received a new GET request");
        return ResponseEntity.ok(apiResponse);
    }

}
