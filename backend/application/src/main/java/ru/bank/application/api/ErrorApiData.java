package ru.bank.application.api;

public class ErrorApiData {
    private final String message;

    public ErrorApiData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
