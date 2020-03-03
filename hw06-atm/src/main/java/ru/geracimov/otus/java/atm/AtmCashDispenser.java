package ru.geracimov.otus.java.atm;

import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;

import java.util.List;
import java.util.Map;

public interface AtmCashDispenser {

    List<AtmCassette> getCassettes();

    Map<Denomination, Long> giveOut(Currency currency, long amount);

    long balance(Currency currency);

    boolean balanceAvailable(Currency currency, long amount);

}
