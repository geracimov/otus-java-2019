package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimpleAtmCashDispenserAcceptor {
    private final SimpleAtmCashDispenser dispenser;

    Map<Currency, Map<Denomination, Long>> accept(Map<Currency, Map<Denomination, Long>> inputCash) {
        Map<Currency, Map<Denomination, Long>> returned = new HashMap<>();
        for (Map.Entry<Currency, Map<Denomination, Long>> inputCashEntry : inputCash.entrySet()) {
            final Currency inputCashCurrency = inputCashEntry.getKey();
            final Map<Denomination, Long> inputCashDenominations = inputCashEntry.getValue();
            acceptCurrency(returned, inputCashCurrency, inputCashDenominations);
        }
        return returned;
    }

    private void acceptCurrency(Map<Currency, Map<Denomination, Long>> returned,
                                Currency inputCashCurrency,
                                Map<Denomination, Long> inputCashDenominations) {
        final List<AtmCassette> cassettesCurrency = cassettesBy(inputCashCurrency);
        if (cassettesCurrency.size() == 0) {
            returned.put(inputCashCurrency, inputCashDenominations);
            return;
        }
        for (Map.Entry<Denomination, Long> denominationsCashEntry : inputCashDenominations.entrySet()) {
            acceptDenomination(returned, inputCashCurrency, denominationsCashEntry);
        }
    }

    private void acceptDenomination(Map<Currency, Map<Denomination, Long>> returned,
                                    Currency inputCashCurrency,
                                    Map.Entry<Denomination, Long> denominationsCashEntry) {
        final List<AtmCassette> denominationCassettes = cassettesBy(inputCashCurrency, denominationsCashEntry.getKey());
        final Map<Denomination, Long> returnedDenominations = returned.getOrDefault(inputCashCurrency, new HashMap<>());
        if (denominationCassettes.size() == 0) {
            returnedDenominations.put(denominationsCashEntry.getKey(), denominationsCashEntry.getValue());
            returned.put(inputCashCurrency, returnedDenominations);
            return;
        }
        //if ATM contains several cassettes of the same denomination, then accept cash in the first cassette
        denominationCassettes.get(0).arrival(denominationsCashEntry.getValue());
    }

    private List<AtmCassette> cassettesBy(Currency currency) {
        return dispenser.getCassetteList().stream()
                .filter(c -> c.loadedCurrency().equals(currency))
                .collect(Collectors.toList());
    }

    private List<AtmCassette> cassettesBy(Currency currency, Denomination denomination) {
        return cassettesBy(currency).stream()
                .filter(c -> c.loadedDenomination().equals(denomination))
                .collect(Collectors.toList());
    }

}
