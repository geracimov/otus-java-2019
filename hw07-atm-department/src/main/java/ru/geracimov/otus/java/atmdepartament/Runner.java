package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.bank.ConfigServer;
import ru.geracimov.otus.java.atmdepartament.bank.RandomConfigServer;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.simple.SimpleAtm;

import java.util.List;

public class Runner {

    public static void main(String[] args) {
        final ConfigServer configServer = new RandomConfigServer();

        final List<AtmBackend> simpleAtmList = List.of(new SimpleAtm(1), new SimpleAtm(2), new SimpleAtm(3), new SimpleAtm(4));

        AtmDepartment department = new AtmDepartmentImpl(configServer);
        department.addAtm(simpleAtmList);

        department.loadAtmConfigurations();
        System.out.println(department.getBalance());

        simpleAtmList.forEach(atm -> {
            ((Atm) atm).userInterface().giveOut(Currency.RUR, 70000);
            ((Atm) atm).userInterface().giveOut(Currency.RUR, 33300);
        });
        System.out.println(department.getBalance());

        department.restoreAtmConfigurations();
        System.out.println(department.getBalance());

    }

}
