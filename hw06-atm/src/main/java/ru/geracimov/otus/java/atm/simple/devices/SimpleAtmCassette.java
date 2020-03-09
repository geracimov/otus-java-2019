package ru.geracimov.otus.java.atm.simple.devices;

import lombok.ToString;
import ru.geracimov.otus.java.atm.AtmCassette;
import ru.geracimov.otus.java.atm.exception.AtmException;
import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;

@ToString
public class SimpleAtmCassette implements AtmCassette {
    private final Currency currency;
    private final Denomination denomination;
    private long balance;

    public SimpleAtmCassette(Currency currency, Denomination denomination, long balance) {
        checkParameters(currency, denomination, balance);
        this.balance = balance;
        this.currency = currency;
        this.denomination = denomination;
    }

    @Override
    public Currency loadedCurrency() {
        return currency;
    }

    @Override
    public Denomination loadedDenomination() {
        return denomination;
    }

    @Override
    public long balance() {
        return balance;
    }

    @Override
    public long withdraw(long count) {
        if (count > balance()) {
            throw new AtmException(String.format("Requested count %d more than balance %d", count, balance));
        }
        balance -= count;
        return balance;
    }

    private void checkParameters(Currency currency, Denomination denomination, long capacity) {
        if (!currency.getDenominations().contains(denomination)) {
            throw new AtmException(String.format("%s banknote %d does not exist", currency.getName(), denomination.getNominal()));
        }
        if (capacity < 0) {
            throw new AtmException(String.format("Balance cannot be less zero: %d", capacity));
        }
    }

}
