package ru.geracimov.otus.java.atmdepartament.devices;


import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

public interface AtmCassette extends Cloneable {

    Currency loadedCurrency();

    Denomination loadedDenomination();

    Long balance();

    Long nominalBalance();

    Long withdraw(long count);

    AtmCassette clone();

}
