package ru.geracimov.otus.java.atm;

import ru.geracimov.otus.java.atm.money.Currency;

public interface AtmKeyboard {

    long chooseAmount();

    Currency chooseCurrency();

}
