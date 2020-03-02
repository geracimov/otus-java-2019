package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import ru.geracimov.otus.java.atmdepartament.bank.GiveOutCalculationStrategy;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@ToString
@RequiredArgsConstructor
public class SimpleAtmCashDispenser implements AtmCashDispenser {
    private final List<AtmCassette> cassettes;
    private final GiveOutCalculationStrategy giveOutStrategy;

    @Override
    @SneakyThrows
    public SimpleAtmCashDispenser clone() {
        final List<AtmCassette> clone = cassettes.stream().map(AtmCassette::clone).collect(Collectors.toUnmodifiableList());
        return new SimpleAtmCashDispenser(clone, giveOutStrategy.clone());
    }

    @Override
    public int getCassettes() {
        return cassettes.size();
    }

    @Override
    public Map<Denomination, Long> giveOut(Currency currency, long amount) {
        final Map<Denomination, Long> calculated = giveOutStrategy.calculate(getLeftovers(currency), amount);
        return withdraw(currency, calculated);
    }

    @SneakyThrows
    @Override
    public void acceptIn(Map<Denomination, Long> input) {
        throw new OperationNotSupportedException("Cannot accept cash");
    }

    @Override
    public long balance(Currency currency) {
        return balanceOptional(currency).orElse(0L);
    }

    @Override
    public Map<Currency, Long> balance() {
        return cassettes.stream().collect(atmCassettesBalanceCollector());
    }

    @Override
    public boolean balanceAvailable(Currency currency, long amount) {
        return balance(currency) > amount;
    }

    private Map<Denomination, Long> getLeftovers(Currency currency) {
        return cassettes.stream()
                .filter(c -> c.loadedCurrency().equals(currency))
                .collect(atmCassettesLeftoversCollector());
    }

    private Collector<AtmCassette, ?, Map<Denomination, Long>> atmCassettesLeftoversCollector() {
        return Collectors.toMap(AtmCassette::loadedDenomination, AtmCassette::balance, Long::sum);
    }

    private Collector<AtmCassette, ?, Map<Currency, Long>> atmCassettesBalanceCollector() {
        return Collectors.toMap(AtmCassette::loadedCurrency, AtmCassette::nominalBalance, Long::sum);
    }

    private Optional<Long> balanceOptional(Currency currency) {
        return cassettes.stream()
                .filter(c -> c.loadedCurrency().equals(currency))
                .map(c -> c.balance() * c.loadedDenomination().getNominal())
                .reduce(Long::sum);
    }

    private Map<Denomination, Long> withdraw(Currency currency, Map<Denomination, Long> calculatedWithdraw) {
        calculatedWithdraw.forEach((denomination, count) -> {
            long restCount = count;
            for (AtmCassette cassette : cassettes) {
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

}
