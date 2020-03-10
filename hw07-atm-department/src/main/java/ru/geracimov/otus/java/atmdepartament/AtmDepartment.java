package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.money.Currency;

import java.util.Collection;
import java.util.Map;

public interface AtmDepartment {

    long getAtmCount();

    Map<Currency, Long> getBalance();

    void restoreAtmConfigurations();

    void loadAtmConfigurations();

    void addAtm(AtmBackend atm);

    void addAtm(Collection<AtmBackend> atmList);

}
