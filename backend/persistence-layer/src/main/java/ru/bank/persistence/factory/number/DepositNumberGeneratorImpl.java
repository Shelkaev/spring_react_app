package ru.bank.persistence.factory.number;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DepositNumberGeneratorImpl implements DepositNumberGenerator {

    private final String creditCardStartNumbers = "11110";
    private final String debitCardStartNumbers = "11112";
    private final String depositStartNumbers = "44444";

    @Override
    public String generateCreditCardNumber() {
         return creditCardStartNumbers +  generateRandomNumber(8) + generateRandomNumber(3);
    }

    @Override
    public String generateDebitCardNumber() {
        return debitCardStartNumbers + generateRandomNumber(8) + generateRandomNumber(3);
    }

    @Override
    public String generateDepositNumber() {
        return depositStartNumbers + generateRandomNumber(8) + generateRandomNumber(7) ;
    }

    private static String generateRandomNumber(int length){
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length);

        Random random = new Random();

        return Integer.toString(random.nextInt(max - min) + min);
    }
}
