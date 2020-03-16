package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.money.Currency;

import java.util.Map;

public interface AtmBackend {

    int getAtmId();

    Map<Currency, Long> getBalance();

    int cassettesLimit();

    void apply(AtmConfiguration configuration);

}
