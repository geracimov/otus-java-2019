package ru.geracimov.otus.java.atmdepartament.devices;

import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.Map;

public interface AtmCashDispenser extends  Cloneable {

    int getCassettes();

    Map<Denomination, Long> giveOut(Currency currency, long amount);

    void acceptIn(Map<Denomination, Long> input);

    long balance(Currency currency);

    Map<Currency, Long> balance();

    boolean balanceAvailable(Currency currency, long amount);

    AtmCashDispenser clone();

}
