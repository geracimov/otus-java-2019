package ru.geracimov.otus.java.atmdepartament.simple;

import lombok.ToString;
import ru.geracimov.otus.java.atmdepartament.Atm;
import ru.geracimov.otus.java.atmdepartament.AtmBackend;
import ru.geracimov.otus.java.atmdepartament.AtmConfiguration;
import ru.geracimov.otus.java.atmdepartament.AtmFacade;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.exception.AtmException;
import ru.geracimov.otus.java.atmdepartament.money.Currency;

import java.util.Map;

@ToString
public class SimpleAtm implements Atm, AtmBackend {
    private final static int CASSETTES_LIMIT = 4;
    private final int atmId;
    private AtmFacade facade;
    private AtmCashDispenser cashDispenser;

    public SimpleAtm(int atmId) {
        this.atmId = atmId;
    }

    @Override
    public void apply(AtmConfiguration configuration) {
        final AtmCashDispenser cashDispenser = configuration.getCashDispenser();

        this.cashDispenser = cashDispenser;
        final int cassettes = cashDispenser.getCassettes();
        if (cassettesLimit() < cassettes) {
            throw new AtmException(String.format("AtmSimple accept %d cassettes max. Your: %d", cassettesLimit(), cassettes));
        }
        this.facade = SimpleAtmFacade.of(configuration.getAtmDisplay(), configuration.getAtmKeyboard(), cashDispenser);
    }

    @Override
    public int cassettesLimit() {
        return CASSETTES_LIMIT;
    }

    @Override
    public final int getAtmId() {
        return atmId;
    }

    @Override
    public Map<Currency, Long> getBalance() {
        return cashDispenser.balance();
    }

    @Override
    public final AtmFacade userInterface() {
        return facade;
    }

}
