package ru.bank.application.api.data.base;

import lombok.Data;

@Data
public class BaseApiResponse {
    private Boolean success;
    private String message;

    public BaseApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
