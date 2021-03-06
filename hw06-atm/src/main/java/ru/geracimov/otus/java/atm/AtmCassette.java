package ru.geracimov.otus.java.atm;


import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;

public interface AtmCassette {

    Currency loadedCurrency();

    Denomination loadedDenomination();

    long balance();

    long withdraw(long count);

}
