package ru.geracimov.otus.java.atm;

public interface Atm {

    AtmFacade userInterface();

    void apply(AtmConfiguration configuration);

    int cassettesLimit();

    int getAtmId();

}
