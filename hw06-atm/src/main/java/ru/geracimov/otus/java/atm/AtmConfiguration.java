package ru.geracimov.otus.java.atm;

public interface AtmConfiguration {

    AtmCashDispenser getCashDispenser();

    AtmDisplay getAtmDisplay();

    AtmKeyboard getAtmKeyboard();

}