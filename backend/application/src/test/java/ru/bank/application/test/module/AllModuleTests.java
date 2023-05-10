package ru.bank.application.test.module;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ru.bank.application.test.module.card.credit.CreditCardServiceTest;
import ru.bank.application.test.module.card.debit.DebitCardServiceTest;
import ru.bank.application.test.module.transfer.TransferServiceTest;
import ru.bank.application.test.module.user.UserServiceTest;


@RunWith(Suite.class)
@SuiteClasses({
    UserServiceTest.class,
    DebitCardServiceTest.class,
    TransferServiceTest.class,
    CreditCardServiceTest.class,
    CreditCardServiceTest.class
})
public class AllModuleTests { }

