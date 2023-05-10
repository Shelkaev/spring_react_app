package ru.bank.application.api.data.registration;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class RegistrationApiRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String patronymic;

    @Min(value = 9999)
    private String password;

}
