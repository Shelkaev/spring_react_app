package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.login.GreetingApiResponse;
import ru.bank.application.api.data.login.LoginApiRequest;
import ru.bank.application.api.data.login.LoginAuthApiResponse;
import ru.bank.application.security.JwtTokenProvider;
import ru.bank.application.service.user.UserService;
import ru.bank.persistence.entity.user.User;

import javax.validation.Valid;


@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity greeting() {
        String title = "Добро пожаловать в онлайн банк";
        String body = "Войти можно по логину и паролю. Если у вас нет учетной записи, предлагаем " +
                "зарегистрироваться.";
        GreetingApiResponse apiResponse = new GreetingApiResponse(title, body);
        log.info("IN LoginController.greeting() - Received a new GET request");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public ResponseEntity login(@Valid @RequestBody LoginApiRequest loginApiRequest) {
        try {
            String login = loginApiRequest.getLogin();
            User user = userService.loadUserByLogin(login);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, loginApiRequest.getPassword()));
            String token = jwtTokenProvider.createToken(login, user.getRole());
            LoginAuthApiResponse apiResponse = new LoginAuthApiResponse("Bearer_" + token, user.getRole().name());
            log.info("IN LoginController.login() - User with login {} was load", user.getLogin());
            return ResponseEntity.ok(apiResponse);
        } catch (RuntimeException e) {
            log.info("IN LoginController.login() - Exception : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseApiResponse(false, e.getMessage()));
        }
    }


}
