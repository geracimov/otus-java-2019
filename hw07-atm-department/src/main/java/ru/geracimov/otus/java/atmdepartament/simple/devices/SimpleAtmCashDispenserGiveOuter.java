package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.atmdepartament.bank.GiveOutCalculationStrategy;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SimpleAtmCashDispenserGiveOuter {
    private final SimpleAtmCashDispenser dispenser;

    Map<Denomination, Long> giveOut(Currency currency, long amount, GiveOutCalculationStrategy giveOutStrategy){
        final Map<Denomination, Long> calculated = giveOutStrategy.calculate(getLeftovers(currency), amount);
        return withdraw(currency, calculated);
    }

    private Map<Denomination, Long> withdraw(Currency currency, Map<Denomination, Long> calculatedWithdraw) {
        calculatedWithdraw.forEach((denomination, count) -> {
            long restCount = count;
            for (AtmCassette cassette : dispenser.getCassetteList()) {
                if (!isLookingCassette(currency, denomination, cassette)) continue;
                final long balance = cassette.balance();
                final long withdrawCassette = balance >= restCount ? restCount : restCount - balance;
                cassette.withdraw(withdrawCassette);
                restCount -= withdrawCassette;
                if (restCount == 0) break;
            }
        });
        return calculatedWithdraw;
    }

    private boolean isLookingCassette(Currency currency, Denomination denomination, AtmCassette cassette) {
        return cassette.loadedCurrency().equals(currency) && cassette.loadedDenomination().equals(denomination);
    }

    private Map<Denomination, Long> getLeftovers(Currency currency) {
        return dispenser.getCassetteList().stream()
                .filter(c -> c.loadedCurrency().equals(currency))
                .collect(atmCassettesLeftoversCollector());
    }

    private Collector<AtmCassette, ?, Map<Denomination, Long>> atmCassettesLeftoversCollector() {
        return Collectors.toMap(AtmCassette::loadedDenomination, AtmCassette::balance, Long::sum);
    }

}
