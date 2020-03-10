package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.devices.AtmDisplay;
import ru.geracimov.otus.java.atmdepartament.devices.AtmKeyboard;

public interface AtmConfiguration extends Cloneable{

    AtmCashDispenser getCashDispenser();

    AtmDisplay getAtmDisplay();

    AtmKeyboard getAtmKeyboard();

    AtmConfiguration clone();

}