package ru.geracimov.otus.java.atmdepartament;

import ru.geracimov.otus.java.atmdepartament.bank.ConfigServer;
import ru.geracimov.otus.java.atmdepartament.bank.RandomConfigServer;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;
import ru.geracimov.otus.java.atmdepartament.simple.SimpleAtm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Runner {

    public static void main(String[] args) {
        final ConfigServer configServer = new RandomConfigServer();
        AtmDepartment department = new AtmDepartmentImpl(configServer);

        final List<AtmBackend> simpleAtmList = List.of(new SimpleAtm(1), new SimpleAtm(2), new SimpleAtm(3), new SimpleAtm(4));
        department.addAtm(simpleAtmList);

        department.loadAtmConfigurations();
        printDepartmentBalance(department);

        final SimpleAtm atm5 = new SimpleAtm(5);
        department.addAtm(atm5);
        final AtmConfiguration atmConfiguration5 = configServer.loadConfiguration(atm5);
        atm5.apply(atmConfiguration5);

        giveOutMoney(simpleAtmList);
        printDepartmentBalance(department);

        department.restoreAtmConfigurations();
        printDepartmentBalance(department);
    }

    private static void giveOutMoney(List<AtmBackend> simpleAtmList) {

        Map<Currency, Map<Denomination, Long>> arrival = new HashMap<>();
        Map<Denomination, Long> arrCur = new HashMap<>();
        arrCur.put(Denomination.B10, 15L);
        arrCur.put(Denomination.B100, 150L);
        arrival.put(Currency.RUR, arrCur);


        simpleAtmList.forEach(atm -> {
            ((Atm) atm).userInterface().giveOut(Currency.RUR, 70000);
            ((Atm) atm).userInterface().giveOut(Currency.RUR, 33300);
            ((Atm) atm).userInterface().giveOut(Currency.USD, 1220);
            ((Atm) atm).userInterface().accept(arrival);
        });
    }

    private static void printDepartmentBalance(AtmDepartment department) {
        System.out.printf("Department Total: %s ATMs, balance: %s\n", +department.getAtmCount(), department.getBalance());
    }

}
