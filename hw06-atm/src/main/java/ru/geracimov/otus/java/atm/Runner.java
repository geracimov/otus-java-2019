package ru.geracimov.otus.java.atm;

import ru.geracimov.otus.java.atm.bank.ConfigServer;
import ru.geracimov.otus.java.atm.bank.RandomConfigServer;
import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.simple.SimpleAtm;

import java.util.List;

public class Runner {

    public static void main(String[] args) {

        ConfigServer configServer = new RandomConfigServer();

        final List<SimpleAtm> simpleAtmList = List.of(new SimpleAtm(1), new SimpleAtm(2), new SimpleAtm(3), new SimpleAtm(4));

        simpleAtmList.forEach(a -> a.apply(configServer.loadConfiguration(a)));

        simpleAtmList.forEach(atm -> {
            atm.userInterface().giveOut(Currency.RUR, 70000);
            atm.userInterface().giveOut(Currency.RUR, 33300);
        });
    }
}
