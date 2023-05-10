package ru.bank.application.api.data.login;

import lombok.Data;

@Data
public class LoginAuthApiResponse {
    private String token;
    private String role;

    public LoginAuthApiResponse(String token,String role) {
        this.token = token;
        this.role = role;
    }

}
