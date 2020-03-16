package ru.geracimov.otus.java.atmdepartament.simple;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.ToString;
import ru.geracimov.otus.java.atmdepartament.AtmConfiguration;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.devices.AtmDisplay;
import ru.geracimov.otus.java.atmdepartament.devices.AtmKeyboard;

@Builder
@Getter
@ToString
public class SimpleAtmConfiguration implements AtmConfiguration {
    private final AtmCashDispenser cashDispenser;
    private final AtmKeyboard atmKeyboard;
    private final AtmDisplay atmDisplay;

    @Override
    @SneakyThrows
    public AtmConfiguration clone() {
        final SimpleAtmConfigurationBuilder builder = builder();
        builder.cashDispenser(cashDispenser.clone())
                .atmKeyboard(atmKeyboard.clone())
                .atmDisplay(atmDisplay.clone());
        return builder.build();
    }
}
