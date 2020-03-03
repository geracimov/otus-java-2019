package ru.geracimov.otus.java.atm.simple;

import lombok.ToString;
import ru.geracimov.otus.java.atm.*;
import ru.geracimov.otus.java.atm.exception.AtmException;

import java.util.List;

@ToString
public class SimpleAtm implements Atm {
    private final static int CASSETTES_LIMIT = 4;
    private final int atmId;
    private AtmFacade facade;

    public SimpleAtm(int atmId) {
        this.atmId = atmId;
    }

    @Override
    public void apply(AtmConfiguration configuration) {
        final AtmCashDispenser cashDispenser = configuration.getCashDispenser();
        final List<AtmCassette> cassettes = cashDispenser.getCassettes();
        if (cassettesLimit() < cassettes.size()) {
            throw new AtmException(String.format("AtmSimple accept %d cassettes max. Your: %d", cassettesLimit(), cassettes.size()));
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
    public final AtmFacade userInterface() {
        return facade;
    }

}
