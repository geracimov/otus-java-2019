package ru.geracimov.otus.java.atmdepartament.bank;

import lombok.NonNull;
import ru.geracimov.otus.java.atmdepartament.AtmBackend;
import ru.geracimov.otus.java.atmdepartament.AtmConfiguration;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;
import ru.geracimov.otus.java.atmdepartament.simple.SimpleAtmConfiguration;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmCassette;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleAtmKeyboard;
import ru.geracimov.otus.java.atmdepartament.simple.devices.SimpleDisplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RandomConfigServer implements ConfigServer {
    private final Random random = new Random();
    private final EnumRandomise<Currency> currencyRandom = new EnumRandomise<>(Currency.class);
    private final EnumRandomise<Denomination> denominationRandom = new EnumRandomise<>(Denomination.class);
    private final Map<AtmBackend, AtmConfiguration> atmConfigurations = new HashMap<>();

    @Override
    public AtmConfiguration loadConfiguration(AtmBackend atm) {
        final List<AtmCassette> randomCassettes = createRandomCassettes(atm.cassettesLimit());
        final GiveOutCalculationStrategy giveOutStrategy = new MinNotesGiveOutStrategy();
        AtmCashDispenser dispenser = new SimpleAtmCashDispenser(randomCassettes, giveOutStrategy);
        final SimpleAtmConfiguration configuration = SimpleAtmConfiguration.builder()
                .cashDispenser(dispenser)
                .atmDisplay(new SimpleDisplay())
                .atmKeyboard(new SimpleAtmKeyboard())
                .build();
        atmConfigurations.put(atm, configuration.clone());
        return configuration;
    }

    @Override
    public void resetAtmConfiguration(@NonNull List<AtmBackend> atmList) {
        Predicate<Map.Entry<AtmBackend, AtmConfiguration>> atmListContainsAtm = t -> atmList.contains(t.getKey());
        atmConfigurations.entrySet()
                .stream()
                .filter(atmListContainsAtm)
                .forEach(entry -> entry.getKey().apply(entry.getValue()));
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
