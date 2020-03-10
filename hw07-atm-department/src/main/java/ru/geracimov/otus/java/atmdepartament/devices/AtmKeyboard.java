package ru.geracimov.otus.java.atmdepartament.devices;

import ru.geracimov.otus.java.atmdepartament.money.Currency;

public interface AtmKeyboard extends  Cloneable {

    long chooseAmount();

    Currency chooseCurrency();

    AtmKeyboard clone();

}
