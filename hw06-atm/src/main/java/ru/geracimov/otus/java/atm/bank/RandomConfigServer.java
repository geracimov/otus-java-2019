package ru.geracimov.otus.java.atm.bank;

import ru.geracimov.otus.java.atm.*;
import ru.geracimov.otus.java.atm.money.Currency;
import ru.geracimov.otus.java.atm.money.Denomination;
import ru.geracimov.otus.java.atm.simple.SimpleAtmConfiguration;
import ru.geracimov.otus.java.atm.simple.devices.SimpleAtmCashDispenser;
import ru.geracimov.otus.java.atm.simple.devices.SimpleAtmCassette;
import ru.geracimov.otus.java.atm.simple.devices.SimpleAtmKeyboard;
import ru.geracimov.otus.java.atm.simple.devices.SimpleDisplay;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RandomConfigServer implements ConfigServer {
    private final Random random = new Random();
    private final EnumRandomise<Currency> currencyRandom = new EnumRandomise<>(Currency.class);
    private final EnumRandomise<Denomination> denominationRandom = new EnumRandomise<>(Denomination.class);

    @Override
    public AtmConfiguration loadConfiguration(Atm atm) {
        final List<AtmCassette> randomCassettes = createRandomCassettes(atm.cassettesLimit());
        final GiveOutCalculationStrategy giveOutStrategy = new MinNotesGiveOutStrategy();
        AtmCashDispenser dispenser = new SimpleAtmCashDispenser(randomCassettes, giveOutStrategy);
        //хочется прям сдесь применить конфигурацию, но мне кажется SRP пальцем грозит...
        //atm.apply(configuration);
        return SimpleAtmConfiguration.builder()
                .cashDispenser(dispenser)
                .atmDisplay(new SimpleDisplay(atm))
                .atmKeyboard(new SimpleAtmKeyboard())
                .build();
    }

    private List<AtmCassette> createRandomCassettes(int count) {
        return Stream.generate(this::createRandomCassette).limit(count).collect(Collectors.toUnmodifiableList());
    }

    private AtmCassette createRandomCassette() {
        final Currency currency = currencyRandom.random();
        final List<Denomination> currencyDenominations = currency.getDenominations();
        final int denominationIndex = randomInt(0, currencyDenominations.size());
        return new SimpleAtmCassette(currency, currencyDenominations.get(denominationIndex), randomInt(10, 100));
    }

    private int randomInt(int from, int to) {
        return random.nextInt(to - from) + from;
    }

}
