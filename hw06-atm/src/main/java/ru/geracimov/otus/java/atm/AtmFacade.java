package ru.geracimov.otus.java.atm;

import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;

import java.util.Map;

public interface AtmFacade {

    Map<Denomination, Long> giveOut();

    Map<Denomination, Long> giveOut(Currency currency, long amount);

}
