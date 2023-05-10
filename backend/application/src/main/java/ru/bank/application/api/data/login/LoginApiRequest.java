package ru.bank.application.api.data.login;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class LoginApiRequest {
    @NotBlank
    private String login;

    @Min(value = 9999)
    private String password;

}
