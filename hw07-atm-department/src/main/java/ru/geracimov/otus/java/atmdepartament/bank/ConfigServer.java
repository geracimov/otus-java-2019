package ru.geracimov.otus.java.atmdepartament.bank;

import ru.geracimov.otus.java.atmdepartament.AtmBackend;
import ru.geracimov.otus.java.atmdepartament.AtmConfiguration;

import java.util.List;

public interface ConfigServer {

    AtmConfiguration loadConfiguration(AtmBackend atm);

    void resetAtmConfiguration();

    void resetAtmConfiguration(List<AtmBackend> atmList);

}
