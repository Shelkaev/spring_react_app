package ru.bank.application.service.credit.currency;



public class CurrencyValue {
    private static final String CURRENCY_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    public static double getUsd() {
        return 66.41;
    }

    public static double getEur() {
        return 71.09;
    }

}
