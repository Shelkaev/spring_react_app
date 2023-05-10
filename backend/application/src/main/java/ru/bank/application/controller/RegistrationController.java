package ru.bank.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bank.application.api.data.base.BaseApiResponse;
import ru.bank.application.api.data.registration.RegistrationApiRequest;
import ru.bank.application.dto.user.SaveUserDto;
import ru.bank.application.service.user.UserService;

import javax.validation.Valid;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity registration(@Valid @RequestBody RegistrationApiRequest request) {
        SaveUserDto saveUserDto = getSaveUserDto(request);
        try {
            userService.registration(saveUserDto);
            log.info("IN RegistrationController.registration() - User with login {} was registered", saveUserDto.getLogin());
            return ResponseEntity.ok(new BaseApiResponse(true, "Пользователь зарегистрирован"));
        } catch (RuntimeException e) {
            log.info("IN RegistrationController.registration() - Exception : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseApiResponse(false, "Неверно заполнена форма"));
        }
    }

    private SaveUserDto getSaveUserDto(RegistrationApiRequest request) {
        SaveUserDto saveUserDto = new SaveUserDto();
        saveUserDto.setLogin(request.getLogin());
        saveUserDto.setName(request.getName());
        saveUserDto.setSurname(request.getSurname());
        saveUserDto.setPatronymic(request.getPatronymic());
        saveUserDto.setPassword(request.getPassword());
        return saveUserDto;
    }

}
