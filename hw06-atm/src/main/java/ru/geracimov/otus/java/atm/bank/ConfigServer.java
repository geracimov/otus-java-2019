package ru.geracimov.otus.java.atm.bank;

import ru.geracimov.otus.java.atm.AtmConfiguration;
import ru.geracimov.otus.java.atm.Atm;

public interface ConfigServer {

    AtmConfiguration loadConfiguration(Atm atm);

}
