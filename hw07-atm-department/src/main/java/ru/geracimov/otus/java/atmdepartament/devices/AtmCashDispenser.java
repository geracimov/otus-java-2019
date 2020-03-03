package ru.geracimov.otus.java.atmdepartament.devices;

import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.Map;

public interface AtmCashDispenser extends Cloneable {

    int getCassettes();

    Map<Denomination, Long> giveOut(Currency currency, long amount);

    Map<Currency, Map<Denomination, Long>> accept(Map<Currency, Map<Denomination, Long>> cash);

    long balance(Currency currency);

    Map<Currency, Long> balance();

    boolean balanceAvailable(Currency currency, long amount);

    AtmCashDispenser clone();

}