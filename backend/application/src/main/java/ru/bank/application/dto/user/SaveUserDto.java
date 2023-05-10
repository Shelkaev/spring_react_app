package ru.bank.application.dto.user;

import lombok.Data;

@Data
public class SaveUserDto {
    private String login;
    private String name;
    private String surname;
    private String patronymic;
    private String password;

}
