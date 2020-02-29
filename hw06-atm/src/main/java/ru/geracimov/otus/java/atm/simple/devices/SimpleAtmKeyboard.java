package ru.geracimov.otus.java.atm.simple.devices;

import lombok.SneakyThrows;
import lombok.ToString;
import ru.geracimov.otus.java.atm.AtmKeyboard;
import ru.geracimov.otus.java.atm.money.Currency;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@ToString(exclude = "reader")
public class SimpleAtmKeyboard implements AtmKeyboard {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    @SneakyThrows
    public long chooseAmount() {
        return Integer.parseInt(reader.readLine());
    }

    @Override
    @SneakyThrows
    public Currency chooseCurrency() {
        return Currency.valueOf(reader.readLine());
    }

}
