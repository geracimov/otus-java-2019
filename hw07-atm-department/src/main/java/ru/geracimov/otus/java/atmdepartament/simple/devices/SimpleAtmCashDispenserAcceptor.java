package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.atmdepartament.money.CashBundle;
import ru.geracimov.otus.java.atmdepartament.money.CashBundleItem;
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

    Map<Currency, CashBundle> accept(Map<Currency, CashBundle> inputCash) {
        Map<Currency, CashBundle> returned = new HashMap<>();
        for (Map.Entry<Currency, CashBundle> inputCashEntry : inputCash.entrySet()) {
            final Currency inputCashCurrency = inputCashEntry.getKey();
            final CashBundle inputCashDenominations = inputCashEntry.getValue();
            acceptCurrency(returned, inputCashCurrency, inputCashDenominations);
        }
        return returned;
    }

    private void acceptCurrency(Map<Currency, CashBundle> returned,
                                Currency inputCashCurrency,
                                CashBundle inputCashBundle) {
        final List<AtmCassette> cassettesCurrency = cassettesBy(inputCashCurrency);
        if (cassettesCurrency.size() == 0) {
            returned.put(inputCashCurrency, inputCashBundle);
            return;
        }
        for (CashBundleItem cashBundleItem : inputCashBundle.getCashBundleItems()) {
            acceptDenomination(returned, inputCashCurrency, cashBundleItem);
        }
    }

    private void acceptDenomination(Map<Currency, CashBundle> returned,
                                    Currency inputCashCurrency,
                                    CashBundleItem inputItem) {
        final List<AtmCassette> denominationCassettes = cassettesBy(inputCashCurrency, inputItem.getDenomination());
        final CashBundle returnedDenominations = returned.getOrDefault(inputCashCurrency, new CashBundle());
        if (denominationCassettes.size() == 0) {
            returnedDenominations.put(inputItem);
            returned.put(inputCashCurrency, returnedDenominations);
            return;
        }
        //if ATM contains several cassettes of the same denomination, then accept cash in the first cassette
        denominationCassettes.get(0).arrival(inputItem.getCount());
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
