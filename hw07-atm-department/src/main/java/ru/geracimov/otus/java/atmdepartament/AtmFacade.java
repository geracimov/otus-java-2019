package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.Map;

public interface AtmFacade {

    Map<Denomination, Long> giveOut();

    Map<Denomination, Long> giveOut(Currency currency, long amount);

}
