package ru.geracimov.otus.java.atm.simple;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.geracimov.otus.java.atm.AtmCashDispenser;
import ru.geracimov.otus.java.atm.AtmConfiguration;
import ru.geracimov.otus.java.atm.AtmDisplay;
import ru.geracimov.otus.java.atm.AtmKeyboard;

@Builder
@Getter
@ToString
public class SimpleAtmConfiguration implements AtmConfiguration {
    private final AtmCashDispenser cashDispenser;
    private final AtmKeyboard atmKeyboard;
    private final AtmDisplay atmDisplay;

}
