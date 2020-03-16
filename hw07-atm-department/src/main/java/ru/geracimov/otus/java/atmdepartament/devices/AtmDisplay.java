package ru.geracimov.otus.java.atmdepartament.devices;


public interface AtmDisplay extends  Cloneable  {

    void printText(String text);

    void printError(String text);

    AtmDisplay clone();

}
