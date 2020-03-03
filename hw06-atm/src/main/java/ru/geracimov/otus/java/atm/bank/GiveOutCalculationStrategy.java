package ru.geracimov.otus.java.atm.bank;

import ru.geracimov.otus.java.atm.money.Denomination;

import java.util.Map;

@FunctionalInterface
public interface GiveOutCalculationStrategy {

    Map<Denomination, Long> calculate(Map<Denomination, Long> leftovers, long amount);

}
