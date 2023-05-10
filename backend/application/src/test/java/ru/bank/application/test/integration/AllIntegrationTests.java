package ru.bank.application.test.integration;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ru.bank.application.test.integration.card.credit.CreditCardAdminIntegrationTest;
import ru.bank.application.test.integration.card.credit.CreditCardUserIntegrationTest;
import ru.bank.application.test.integration.card.debit.DebitCardIntegrationTest;
import ru.bank.application.test.integration.login.LoginIntegrationTest;
import ru.bank.application.test.integration.main.MainIntegrationTest;
import ru.bank.application.test.integration.registration.RegistrationIntegrationTest;
import ru.bank.application.test.integration.transfer.TransferIntegrationTest;



@RunWith(Suite.class)
@SuiteClasses({
    RegistrationIntegrationTest.class,
    LoginIntegrationTest.class,
    DebitCardIntegrationTest.class,
    TransferIntegrationTest.class,
    CreditCardUserIntegrationTest.class,
    CreditCardAdminIntegrationTest.class,
    MainIntegrationTest.class
})
public class AllIntegrationTests { }

