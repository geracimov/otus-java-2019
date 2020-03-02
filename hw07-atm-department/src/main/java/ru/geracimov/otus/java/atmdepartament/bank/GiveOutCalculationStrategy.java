package ru.geracimov.otus.java.atmdepartament.bank;

import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.Map;

public interface GiveOutCalculationStrategy extends Cloneable{

    Map<Denomination, Long> calculate(Map<Denomination, Long> leftovers, long amount);

    GiveOutCalculationStrategy clone();

}
