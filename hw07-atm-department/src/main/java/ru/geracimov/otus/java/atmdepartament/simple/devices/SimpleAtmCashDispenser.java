package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import ru.geracimov.otus.java.atmdepartament.money.CashBundle;
import ru.geracimov.otus.java.atmdepartament.bank.GiveOutCalculationStrategy;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCashDispenser;
import ru.geracimov.otus.java.atmdepartament.devices.AtmCassette;
import ru.geracimov.otus.java.atmdepartament.money.Currency;
import ru.geracimov.otus.java.atmdepartament.money.Denomination;

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
    private final SimpleAtmCashDispenserAcceptor acceptor = new SimpleAtmCashDispenserAcceptor(this);
    private final SimpleAtmCashDispenserGiveOuter giveOuter = new SimpleAtmCashDispenserGiveOuter(this);

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
        return giveOuter.giveOut(currency, amount, giveOutStrategy);
    }

    @Override
    public Map<Currency, CashBundle> acceptCashBundles(Map<Currency, CashBundle> inputCashBundles) {
        return acceptor.accept(inputCashBundles);
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

    List<AtmCassette> getCassetteList() {
        return cassettes;
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

}
